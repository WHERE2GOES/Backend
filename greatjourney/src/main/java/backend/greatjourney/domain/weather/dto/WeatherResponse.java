package backend.greatjourney.domain.weather.dto;


import lombok.Data;

@Data
public class WeatherResponse {
    //@Data에는 getter setter가 기본적으로 달려있는 형식인 것이다.

    // 페이지 번호
    private String pageNo;

    // 전체 결과 수
    private String totalCount;

    // 발표일자
    private String baseDate;

    // 발표 시각
    private String baseTime;

    // 예보 지점 X 좌표
    private String nx;

    // 예보 지점 Y 좌표
    private String ny;

    // 자료 구분 코드 (RN1, T1H 등)
    private String category;

    // 실황 값 (강수량, 기온 등)
    private String obsrValue;

}
