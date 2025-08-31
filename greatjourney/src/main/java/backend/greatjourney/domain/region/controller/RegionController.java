package backend.greatjourney.domain.region.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.region.dto.RelatedPlaceDto;
import backend.greatjourney.domain.region.service.RegionService;
import backend.greatjourney.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/region")
@RequiredArgsConstructor
@Tag(name = "지역 근처의 음식점, 숙소, 관광지, 포토스팟을 가져오는 API입니다.")
@Slf4j
public class RegionController {

	private final RegionService regionService;


	@GetMapping("/food")
	@Operation(description = "주변 음식점을 가져오는 API입니다")
	public BaseResponse<List<RelatedPlaceDto>> getFoods(@RequestParam String areaName, @RequestParam String sigunguName){
		//아니면 pageable로 무한 스크롤 가능하게 수정해도 됨
		return BaseResponse.<List<RelatedPlaceDto>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 음식점 정보를 가져왔습니다.")
			.data(regionService.getRegions(areaName,sigunguName,"food"))
			.build();
	}

	@GetMapping("/sleep")
	@Operation(description = "주변 숙소를 가져오는 API입니다")
	public BaseResponse<List<RelatedPlaceDto>> getSleep(@RequestParam String areaName, @RequestParam String sigunguName){
		//아니면 pageable로 무한 스크롤 가능하게 수정해도 됨
		return BaseResponse.<List<RelatedPlaceDto>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 음식점 정보를 가져왔습니다.")
			.data(regionService.getRegions(areaName,sigunguName,"sleep"))
			.build();
	}

	@GetMapping("/tour")
	@Operation(description = "주변 관광지를 가져오는 API입니다")
	public BaseResponse<List<RelatedPlaceDto>> getTour(@RequestParam String areaName, @RequestParam String sigunguName){
		//아니면 pageable로 무한 스크롤 가능하게 수정해도 됨

		return BaseResponse.<List<RelatedPlaceDto>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 음식점 정보를 가져왔습니다.")
			.data(regionService.getRegions(areaName,sigunguName,"tour"))
			.build();
	}


	@GetMapping("/photo")
	@Operation(description = "주변 포토스팟를 가져오는 API입니다")
	public BaseResponse<List<RelatedPlaceDto>> getPhoto(@RequestParam String areaName, @RequestParam String sigunguName){
		//아니면 pageable로 무한 스크롤 가능하게 수정해도 됨

		return BaseResponse.<List<RelatedPlaceDto>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 포토스팟 정보를 가져왔습니다.")
			.data(regionService.getRegions(areaName,sigunguName,"photo"))
			.build();
	}

}
