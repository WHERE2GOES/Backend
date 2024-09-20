package backend.greatjourney.domain.weather.dto;

import lombok.Data;

@Data
public class WeatherRequest {

    //X와 Y좌표
    private String locationX;

    private String locationY;

    //페이지 번호
    private String pageNo;

    private String numOfRows;

    private String base_date;

    private String base_time;

}
