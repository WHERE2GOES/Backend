package backend.greatjourney.domain.weather.dto;


import backend.greatjourney.domain.weather.entity.PrecipitationType;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UltraNcstKoreanDto {
	// 메타
	private String baseDate;      // baseDate (YYYYMMDD)
	private String baseTime;      // baseTime (HHMM)
	private Integer X;        // nx
	private Integer Y;        // ny

	// 관측값(카테고리 한글화)
	private Double  temperature;         // T1H (℃)
	private Integer humidity;         // REH (%)
	private Double  windSpeed;         // WSD (m/s)
	private Integer windDircetion;         // VEC (deg)
	private PrecipitationType rainType; // Enum 사용
	private Double  RN1;   // RN1 (mm)
	private Double  UUU; // UUU (m/s)
	private Double  VVV; // VVV (m/s)
}

