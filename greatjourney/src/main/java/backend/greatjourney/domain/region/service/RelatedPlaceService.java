package backend.greatjourney.domain.region.service;

import backend.greatjourney.config.client.RegionApiClient;
import backend.greatjourney.domain.region.dto.RegionApiResponse;
import backend.greatjourney.domain.region.dto.RelatedPlaceDto;
import backend.greatjourney.domain.region.entity.PlaceCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatedPlaceService {

	private final RegionApiClient regionApiClient;

	@Cacheable(
		cacheNames = "relatedPlaces",
		// ⚠️ TOUR와 PHOTO는 lcls가 모두 '관광지'이므로 kor를 키로 쓰면 충돌 → name() 사용
		key = "T(String).format('%s:%s:%s:%s:%s:%s', #baseYm, #areaCd, #signguCd, #pageNo, #numOfRows, #category.name())"
	)
	public List<RelatedPlaceDto> getRelatedPlacesByCategory(
		String baseYm, String areaCd, String signguCd,
		int pageNo, int numOfRows, PlaceCategory category
	) {
		var res = regionApiClient.callAreaBasedList(baseYm, areaCd, signguCd, pageNo, numOfRows);
		var items = (res != null && res.getResponse()!=null
			&& res.getResponse().getBody()!=null
			&& res.getResponse().getBody().getItems()!=null)
			? res.getResponse().getBody().getItems().getItem()
			: List.<RegionApiResponse.Item>of();

		return items.stream()
			.filter(category::matches) // ← enum에 위임 (PHOTO는 lcls+mcls 동시 필터)
			.map(i -> new RelatedPlaceDto(
				i.getRlteTatsNm(),
				i.getRlteRank(),
				i.getRlteRegnNm(),
				i.getRlteSignguNm(),
				i.getRlteCtgryLclsNm(),
				i.getRlteCtgryMclsNm(),
				i.getRlteCtgrySclsNm()
			))
			.collect(Collectors.toList());
	}
}
