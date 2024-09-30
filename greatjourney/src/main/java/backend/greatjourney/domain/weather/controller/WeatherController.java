package backend.greatjourney.domain.weather.controller;

import backend.greatjourney.domain.weather.dto.WeatherRequest;
import backend.greatjourney.domain.weather.dto.WeatherResponse;
import backend.greatjourney.domain.weather.service.WeatherService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/api/weather/short")
    public BaseResponse getWeathers(@RequestHeader(name = "Authorization")String token, @RequestParam String Step1, @RequestParam String Step2, @RequestParam @Nullable String Step3) throws IOException {

        WeatherResponse weatherResponse = weatherService.getWeather(Step1,Step2,Step3,"5","20");

        if (weatherResponse == null ) {
            return BaseResponse.builder()
                    .data(null)
                    .code(500)
                    .isSuccess(false)
                    .message("단기예보를 가져오기를 실패했습니다.")
                    .build();
        }

        return BaseResponse.builder()
                .data(weatherResponse)
                .code(200)
                .isSuccess(true)
                .message("단기예보를 가져왔습니다.")
                .build();
    }
}
