package backend.greatjourney.domain.weather.service;


import backend.greatjourney.domain.weather.dto.GridRequest;
import backend.greatjourney.domain.weather.dto.GridResponse;
import backend.greatjourney.domain.weather.dto.KmaUltraNcstDto;
import backend.greatjourney.domain.weather.dto.KmaUltraNcstMapper;
import backend.greatjourney.domain.weather.dto.UltraNcstKoreanDto;
import backend.greatjourney.domain.weather.dto.WeatherResponse;
import backend.greatjourney.domain.weather.entity.WeatherGrid;
import backend.greatjourney.domain.weather.repository.WeatherGridRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherGridService {

	private final WeatherGridRepository repository;
	private final WebClient kmaClient; // Bean 이름과 일치

	@Value("${tour.key}")
	private String serviceKey;

	@Transactional(readOnly = true)
	public GridResponse getNxNy(GridRequest req) {
		String l1 = norm(req.step1());
		String l2 = norm(req.step2());
		String l3 = norm(req.step3());

		// 1) 정확 일치
		var exact = repository.findFirstByLevel1AndLevel2AndLevel3(l1, l2, l3);
		if (exact.isPresent()) return toResponse(exact.get());

		// 2) 3단계가 비어있는(빈 문자열) 레코드
		if (l2 != null) {
			var l3Empty = repository.findFirstByLevel1AndLevel2AndLevel3(l1, l2, "");
			if (l3Empty.isPresent()) return toResponse(l3Empty.get());

			// 3) 3단계가 NULL인 레코드
			var l3Null = repository.findFirstByLevel1AndLevel2AndLevel3IsNull(l1, l2);
			if (l3Null.isPresent()) return toResponse(l3Null.get());
		}

		// 4) 2/3단계 모두 비어있는 상위만 매칭 (NULL 기준)
		var upperNull = repository.findFirstByLevel1AndLevel2IsNullAndLevel3IsNull(l1);
		if (upperNull.isPresent()) return toResponse(upperNull.get());

		// 5) 마지막 폴백: level1만 같은 것 중 임의 하나
		return repository.findByLevel1(l1).stream().findFirst()
			.map(this::toResponse)
			.orElseThrow(() -> new IllegalArgumentException(
				"행정구역 매칭 실패: %s > %s > %s".formatted(l1, l2, l3)));
	}

	private GridResponse toResponse(WeatherGrid g) {
		return new GridResponse(g.getNx(), g.getNy());
	}

	private String norm(String s) {
		if (s == null) return null;
		String v = s.trim();
		if (v.isEmpty()) return "";        // 빈 문자열과 NULL을 구분
		return v;                          // (필요하면 공백제거/소문자화 규칙 추가)
	}



	//날씨 가져오는 API
	//관광 정보 데이터를 가져와서 처리합니다.
	public UltraNcstKoreanDto getWeather(GridRequest request){
		GridResponse response = getNxNy(request);

		return fetchKorean(response.nx(),response.ny());
	}


	private static String[] resolveBaseDateTime() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		if (now.getMinute() < 40) now = now.minusHours(1);
		String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String baseTime = now.format(DateTimeFormatter.ofPattern("HH")) + "00";
		return new String[]{baseDate, baseTime};
	}

	public KmaUltraNcstDto fetchRaw(int nx, int ny) {
		String[] dt = resolveBaseDateTime();
		return kmaClient.get()
			.uri(b -> b.path("/getUltraSrtNcst")
				.queryParam("serviceKey", serviceKey)
				.queryParam("pageNo", 1)
				.queryParam("numOfRows", 100)
				.queryParam("dataType", "JSON")
				.queryParam("base_date", dt[0])
				.queryParam("base_time", dt[1])
				.queryParam("nx", nx)
				.queryParam("ny", ny)
				.build())
			.retrieve()
			.bodyToMono(KmaUltraNcstDto.class)
			.block();
	}

	/** 한국어 DTO로 변환하여 반환 */
	public UltraNcstKoreanDto fetchKorean(int nx, int ny) {
		var raw = fetchRaw(nx, ny);
		var header = raw.getResponse().getHeader();
		if (!"00".equals(header.getResultCode())) {
			throw new IllegalStateException("KMA error: " + header.getResultCode() + " - " + header.getResultMsg());
		}
		return KmaUltraNcstMapper.toKorean(raw);
	}

}
