package backend.greatjourney.domain.festival.dto;

// src/main/java/backend/greatjourney/tour/dto/FestivalSearchDto.java


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FestivalSearchDto {

	@JsonProperty("response")
	private Response response;

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Response {
		@JsonProperty("header") private Header header;
		@JsonProperty("body")   private Body body;
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Header {
		@JsonProperty("resultCode") private String resultCode; // "0000"
		@JsonProperty("resultMsg")  private String resultMsg;  // "OK"
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Body {
		@JsonProperty("items")     private Items items;
		@JsonProperty("numOfRows") private Integer numOfRows;
		@JsonProperty("pageNo")    private Integer pageNo;
		@JsonProperty("totalCount")private Integer totalCount;
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	public static class Items {
		@JsonProperty("item") private List<Item> item;
	}

	@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private Integer rnum;

		private String addr1;
		private String addr2;
		private String zipcode;

		private String cat1;
		private String cat2;
		private String cat3;

		private String contentid;
		private String contenttypeid;

		private String createdtime;

		private String eventstartdate; // YYYYMMDD
		private String eventenddate;   // YYYYMMDD

		private String firstimage;
		private String firstimage2;
		private String cpyrhtDivCd;

		private String mapx; // 문자열로 옴 (필요시 Double로 변환)
		private String mapy;
		private String mlevel;

		private String modifiedtime;

		private String areacode;
		private String sigungucode;

		private String tel;
		private String title;

		private String lDongRegnCd;
		private String lDongSignguCd;
		private String lclsSystm1;
		private String lclsSystm2;
		private String lclsSystm3;

		private String progresstype;
		private String festivaltype;
	}
}
