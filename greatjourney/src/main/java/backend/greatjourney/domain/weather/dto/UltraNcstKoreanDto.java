package backend.greatjourney.domain.weather.dto;


import backend.greatjourney.domain.weather.entity.PrecipitationType;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UltraNcstKoreanDto {
	// 메타
	private String 기준일자;      // baseDate (YYYYMMDD)
	private String 기준시각;      // baseTime (HHMM)
	private Integer 격자X;        // nx
	private Integer 격자Y;        // ny

	// 관측값(카테고리 한글화)
	private Double  기온;         // T1H (℃)
	private Integer 습도;         // REH (%)
	private Double  풍속;         // WSD (m/s)
	private Integer 풍향;         // VEC (deg)
	private PrecipitationType 강수형태; // Enum 사용
	private Double  시간강수량;   // RN1 (mm)
	private Double  동서바람성분; // UUU (m/s)
	private Double  남북바람성분; // VVV (m/s)
}

