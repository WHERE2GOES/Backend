package backend.greatjourney.domain.festival.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import backend.greatjourney.domain.festival.dto.FestivalResponse;
import backend.greatjourney.domain.festival.dto.FestivalSearchDto;
import backend.greatjourney.domain.region.entity.KukmoonRegion;
import backend.greatjourney.domain.region.repository.KukmoonRegionRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FestivalService {

	private final WebClient tourClient;
	private final KukmoonRegionRepository kukmoonRegionRepository;

	@Value("${tour.key}")
	private String serviceKey;

	private String changeGang(String areaName){
		if(areaName.contains("강원")) {
			return "강원특별자치도";
		}else if(areaName.contains("경기")){
			return "경기도";
		}else{
			return areaName;
		}
	}


	//일단 지역 입력받기 -> 지역을 바탕으로 정보들을 가져오기
	public List<FestivalResponse> getFestivals(String eventStartDate, String areaName){
		KukmoonRegion kukmoonRegion = kukmoonRegionRepository.findByName(changeGang(areaName))
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));

		FestivalSearchDto dto = callSearchFestival2(eventStartDate, kukmoonRegion.getCode());

		return changeDto(dto);

	}

	public FestivalSearchDto callSearchFestival2(String eventStartDate,Long areaCode) {
		return tourClient.get()
			.uri(b -> b.path("/B551011/KorService2/searchFestival2")
				.queryParam("numOfRows", 100)
				.queryParam("pageNo", 0)
				.queryParam("MobileOS", "AND")
				.queryParam("MobileApp", "eodiro")
				.queryParam("_type", "json")
				.queryParam("arrange", "A")
				.queryParam("eventStartDate", eventStartDate)
				.queryParam("serviceKey", serviceKey)
				.queryParam("areaCode", areaCode)
				.build())
			.retrieve()
			.bodyToMono(FestivalSearchDto.class)
			.block();
	}

	public List<FestivalResponse> getFood(String areaName){
		KukmoonRegion kukmoonRegion = kukmoonRegionRepository.findByName(changeGang(areaName))
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));

		FestivalSearchDto dto = callSearch("39",kukmoonRegion.getCode());
		return changeDto(dto);
	}

	public List<FestivalResponse> getHotel(String areaName){
		KukmoonRegion kukmoonRegion = kukmoonRegionRepository.findByName(changeGang(areaName))
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));
		FestivalSearchDto dto = callSearch("32",kukmoonRegion.getCode());
		return changeDto(dto);
	}

	public List<FestivalResponse> getPhoto(String areaName){
		KukmoonRegion kukmoonRegion = kukmoonRegionRepository.findByName(changeGang(areaName))
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));
		FestivalSearchDto dto = callSearch("12",kukmoonRegion.getCode());
		return changeDto(dto);
	}

	public List<FestivalResponse> getPlay(String areaName){
		KukmoonRegion kukmoonRegion = kukmoonRegionRepository.findByName(changeGang(areaName))
			.orElseThrow(()->new CustomException(ErrorCode.NO_REGION));
		FestivalSearchDto dto = callSearch("14",kukmoonRegion.getCode());
		return changeDto(dto);
	}


	public FestivalSearchDto callSearch(String contentId,Long areaCode) {
		return tourClient.get()
			.uri(b -> b.path("/B551011/KorService2/areaBasedList2")
				.queryParam("numOfRows", 100)
				.queryParam("pageNo", 0)
				.queryParam("MobileOS", "AND")
				.queryParam("MobileApp", "eodiro")
				.queryParam("_type", "json")
				.queryParam("arrange", "A")
				.queryParam("contentTypeId", contentId)
				.queryParam("serviceKey", serviceKey)
				.queryParam("areaCode", areaCode)
				.build())
			.retrieve()
			.bodyToMono(FestivalSearchDto.class)
			.block();
	}








	private List<FestivalResponse> changeDto(FestivalSearchDto dto){
		if (dto == null
			|| dto.getResponse() == null
			|| dto.getResponse().getBody() == null
			|| dto.getResponse().getBody().getItems() == null
			|| dto.getResponse().getBody().getItems().getItem() == null) {
			return List.of();
		}

		return dto.getResponse().getBody().getItems().getItem().stream()
			.map(it -> new FestivalResponse(
				it.getAddr1(),
				it.getAddr2(),
				it.getZipcode(),
				it.getContentid(),
				it.getCreatedtime(),
				it.getTel(),
				it.getTitle(),
				it.getFirstimage(),
				it.getFirstimage2(),
				it.getMapx(),
				it.getMapy(),
				it.getEventstartdate(),
				it.getEventenddate()
			))
			.toList();
	}



}
