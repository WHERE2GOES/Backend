package backend.greatjourney.domain.weather.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WeatherResponse {
    //@Data에는 getter setter가 기본적으로 달려있는 형식인 것이다.

    private String precipitationProbability; // 강수확률 (POP)
    private String humidity;                 // 습도 (REH)
    private String precipitationType;        // 강수형태 (PTY)
    private String temperature;              // 기온 (TMP)
    private String windSpeed;                // 풍속 (WSD)
    private String skyCondition;             // 하늘상태 (SKY)

}
