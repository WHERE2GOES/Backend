package backend.greatjourney.domain.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.weather.dto.GridRequest;
import backend.greatjourney.domain.weather.dto.UltraNcstKoreanDto;
import backend.greatjourney.domain.weather.service.WeatherGridService;
import backend.greatjourney.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Tag(name = "weather controller")
public class WeatherController {
	private final WeatherGridService weatherGridService;


	@GetMapping()
	@Operation(description = "날씨 정보를 가져오는 API입니다.")
	public BaseResponse<UltraNcstKoreanDto> getWeather(@RequestParam String step1,
		@RequestParam(required = false) String step2,
		@RequestParam(required = false) String step3){
		return BaseResponse.<UltraNcstKoreanDto>builder()
			.message("날씨 정보를 조회했습니다.")
			.isSuccess(true)
			.code(200)
			.data(weatherGridService.getWeather(new GridRequest(step1, step2, step3)))
			.build();
	}

}
