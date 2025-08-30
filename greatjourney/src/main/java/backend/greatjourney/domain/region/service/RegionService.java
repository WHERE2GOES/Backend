package backend.greatjourney.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import backend.greatjourney.domain.region.dto.RelatedPlaceDto;
import backend.greatjourney.domain.region.entity.PlaceCategory;
import backend.greatjourney.domain.region.entity.Region;
import backend.greatjourney.domain.region.repository.RegionRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionService {

	private final RegionRepository regionRepository;
	private final RelatedPlaceService placeService;

	//일단 좌표 시군구 변환 필요하면 추가하면 됨
	//각각의 태그 요청값에 따라 다르게 보내주면 됨
	public List<RelatedPlaceDto> getRegions( String areaName,String sigunguName, String tags){

		Region region = regionRepository.findByAreaNmAndSigunguNm(areaName,sigunguName)
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));

		return placeService.getRelatedPlacesByCategory("202504",region.getAreaCd().toString(),region.getSigunguCd().toString()
		,0,100, PlaceCategory.valueOf(tags));
	}


}
