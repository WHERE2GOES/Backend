package backend.greatjourney.domain.weather.controller;

import backend.greatjourney.domain.weather.dto.WeatherRequest;
import backend.greatjourney.domain.weather.dto.WeatherResponse;
import backend.greatjourney.domain.weather.service.WeatherService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/api/weather/short")
    public BaseResponse getWeathers(@RequestBody WeatherRequest request) throws IOException {

        List<WeatherResponse> weatherResponse = weatherService.getWeather(request);

        return BaseResponse.builder()
                .data(weatherResponse)
                .code(200)
                .isSuccess(true)
                .message("단기예보를 가져왔습니다.")
                .build();
    }
}
