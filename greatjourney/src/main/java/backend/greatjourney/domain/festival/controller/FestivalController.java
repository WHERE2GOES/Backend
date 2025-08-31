package backend.greatjourney.domain.festival.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.festival.dto.FestivalResponse;
import backend.greatjourney.domain.festival.service.FestivalService;
import backend.greatjourney.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/near")
@RequiredArgsConstructor
public class FestivalController {

	private final FestivalService festivalService;


	@GetMapping("/festival")
	@Operation(description = "서울"
		+ " 인천"
		+ " 대전"
		+ " 대구"
		+ " 광주"
		+ " 부산"
		+ " 울산"
		+ " 세종특별자치시"
		+ " 경기도"
		+ " 강원특별자치도"
		+ " 충청북도"
		+ " 충청남도"
		+ " 경상북도"
		+ " 경상남도"
		+ " 전북특별자치도"
		+ " 전라남도"
		+ " 제주특별자치도\n" +"로 값들을 넣어주면 됨 + 날짜")
	public BaseResponse<List<FestivalResponse>> getFestivals(@RequestParam String areaName, @RequestParam String startDate){
		return BaseResponse.<List<FestivalResponse>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 축제 정보를 가져왔습니다.")
			.data(festivalService.getFestivals(startDate,areaName))
			.build();

	}


	@GetMapping("/food")
	@Operation(description = "서울"
		+ " 인천"
		+ " 대전"
		+ " 대구"
		+ " 광주"
		+ " 부산"
		+ " 울산"
		+ " 세종특별자치시"
		+ " 경기도"
		+ " 강원특별자치도"
		+ " 충청북도"
		+ " 충청남도"
		+ " 경상북도"
		+ " 경상남도"
		+ " 전북특별자치도"
		+ " 전라남도"
		+ " 제주특별자치도\n" +"로 값들을 넣어주면 됨 + 날짜")
	public BaseResponse<List<FestivalResponse>> getFood(@RequestParam String areaName){
		return BaseResponse.<List<FestivalResponse>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 음식점 정보를 가져왔습니다.")
			.data(festivalService.getFood(areaName))
			.build();

	}


	@GetMapping("/hotel")
	@Operation(description = "서울"
		+ " 인천"
		+ " 대전"
		+ " 대구"
		+ " 광주"
		+ " 부산"
		+ " 울산"
		+ " 세종특별자치시"
		+ " 경기도"
		+ " 강원특별자치도"
		+ " 충청북도"
		+ " 충청남도"
		+ " 경상북도"
		+ " 경상남도"
		+ " 전북특별자치도"
		+ " 전라남도"
		+ " 제주특별자치도\n" +"로 값들을 넣어주면 됨 + 날짜")
	public BaseResponse<List<FestivalResponse>> getHotel(@RequestParam String areaName){
		return BaseResponse.<List<FestivalResponse>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 호텔 정보를 가져왔습니다.")
			.data(festivalService.getHotel(areaName))
			.build();

	}


	@GetMapping("/play")
	@Operation(description = "서울"
		+ " 인천"
		+ " 대전"
		+ " 대구"
		+ " 광주"
		+ " 부산"
		+ " 울산"
		+ " 세종특별자치시"
		+ " 경기도"
		+ " 강원특별자치도"
		+ " 충청북도"
		+ " 충청남도"
		+ " 경상북도"
		+ " 경상남도"
		+ " 전북특별자치도"
		+ " 전라남도"
		+ " 제주특별자치도\n" +"로 값들을 넣어주면 됨 + 날짜")
	public BaseResponse<List<FestivalResponse>> getPlay(@RequestParam String areaName){
		return BaseResponse.<List<FestivalResponse>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 놀거리 정보를 가져왔습니다.")
			.data(festivalService.getPlay(areaName))
			.build();

	}


	@GetMapping("/photo")
	@Operation(description = "서울"
		+ " 인천"
		+ " 대전"
		+ " 대구"
		+ " 광주"
		+ " 부산"
		+ " 울산"
		+ " 세종특별자치시"
		+ " 경기도"
		+ " 강원특별자치도"
		+ " 충청북도"
		+ " 충청남도"
		+ " 경상북도"
		+ " 경상남도"
		+ " 전북특별자치도"
		+ " 전라남도"
		+ " 제주특별자치도\n" +"로 값들을 넣어주면 됨 + 날짜")
	public BaseResponse<List<FestivalResponse>> getPhoto(@RequestParam String areaName){
		return BaseResponse.<List<FestivalResponse>>builder()
			.code(200)
			.isSuccess(true)
			.message("주변 포토스팟 정보를 가져왔습니다.")
			.data(festivalService.getPhoto(areaName))
			.build();

	}



}
