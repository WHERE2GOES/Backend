package backend.greatjourney.domain.weather.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KmaUltraNcstDto {

	@JsonProperty("response")
	private Response response;

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Response {
		@JsonProperty("header") private Header header;
		@JsonProperty("body")   private Body body;
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Header {
		@JsonProperty("resultCode") private String resultCode; // "00" 정상
		@JsonProperty("resultMsg")  private String resultMsg;  // "NORMAL_SERVICE"
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Body {
		@JsonProperty("dataType")  private String dataType;   // "JSON"
		@JsonProperty("pageNo")    private Integer pageNo;
		@JsonProperty("numOfRows") private Integer numOfRows;
		@JsonProperty("totalCount")private Integer totalCount;
		@JsonProperty("items")     private Items items;
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Items {
		@JsonProperty("item") private List<Item> item;
	}

	// 개별 관측항목
	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		@JsonProperty("baseDate")  private String baseDate;   // YYYYMMDD
		@JsonProperty("baseTime")  private String baseTime;   // HHMM (정시)
		@JsonProperty("nx")        private Integer nx;
		@JsonProperty("ny")        private Integer ny;
		@JsonProperty("category")  private String category;   // RN1, T1H, UUU, VVV, WSD, REH, PTY, VEC 등
		@JsonProperty("obsrValue") private String obsrValue;  // 실수/정수 모두 문자열로 옴
	}
}
