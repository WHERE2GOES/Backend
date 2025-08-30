package backend.greatjourney.domain.region.service;

// RelatedPlaceService.java
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

	@Cacheable(cacheNames = "relatedPlaces",
		key = "T(String).format('%s:%s:%s:%s:%s:%s', #baseYm, #areaCd, #signguCd, #pageNo, #numOfRows, #category.kor)")
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
			.filter(i -> category.getKor().equals(i.getRlteCtgryLclsNm()))
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
