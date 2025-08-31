package backend.greatjourney.domain.weather.service;


import backend.greatjourney.domain.weather.dto.GridRequest;
import backend.greatjourney.domain.weather.dto.GridResponse;
import backend.greatjourney.domain.weather.entity.WeatherGrid;
import backend.greatjourney.domain.weather.repository.WeatherGridRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherGridService {

	private final WeatherGridRepository repository;

	public GridResponse getNxNy(GridRequest req) {
		String l1 = norm(req.step1());
		String l2 = norm(req.step2());
		String l3 = norm(req.step3());

		// 1) 정확 일치 (1,2,3)
		Optional<WeatherGrid> exact = repository.findFirstByLevel1AndLevel2AndLevel3(l1, l2, l3);
		if (exact.isPresent()) return toResponse(exact.get());

		// 2) 3단계 비어있는 경우로 폴백 (1,2,"" or null)
		Optional<WeatherGrid> l3blank = repository.findFirstByLevel1AndLevel2AndLevel3In(
			l1, l2, List.of("", null));
		if (l3blank.isPresent()) return toResponse(l3blank.get());

		// 3) 2/3단계 모두 비어있는 상위만 매칭 (1,"","")
		Optional<WeatherGrid> upperOnly = repository.findFirstByLevel1AndLevel2InAndLevel3In(
			l1, List.of("", null), List.of("", null));
		if (upperOnly.isPresent()) return toResponse(upperOnly.get());

		// 4) 마지막 안전장치: level1만 같은 것들 중 대표 하나 선택
		return repository.findByLevel1(l1).stream().findFirst()
			.map(this::toResponse)
			.orElseThrow(() -> new IllegalArgumentException(
				"해당 행정구역을 찾을 수 없습니다: %s > %s > %s".formatted(l1, l2, l3)));
	}

	private GridResponse toResponse(WeatherGrid g) {
		return new GridResponse(g.getNx(), g.getNy());
	}

	private String norm(String s) {
		if (s == null) return null;
		String v = s.trim();
		if (v.isEmpty()) return "";
		// 데이터 정합성 향상을 위해 일반화 처리(선택):
		// 광역명/구분 띄어쓰기·대소문자 차이를 흡수
		return v.replaceAll("\\s+", " ")
			.replace("도 ", "도 ")
			.replace("시 ", "시 ")
			.toLowerCase(Locale.KOREA)
			.replace(" ", ""); // 엑셀 값이 공백 없이 저장된 경우가 많아 무공백 비교
	}
}
