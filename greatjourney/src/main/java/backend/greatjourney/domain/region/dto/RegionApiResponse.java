package backend.greatjourney.domain.region.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter; import lombok.Setter;
import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionApiResponse {
	private Response response;

	@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
	public static class Response {
		private Header header;
		private Body body;
	}

	@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
	public static class Header {
		private String resultCode;
		private String resultMsg;
	}

	@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body {
		private Items items;
		private Integer numOfRows;
		private Integer pageNo;
		private Integer totalCount;
	}

	@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
	public static class Items {
		private List<Item> item;
	}

	@Getter @Setter @JsonIgnoreProperties(ignoreUnknown = true)
	public static class Item {
		private String baseYm;

		@JsonProperty("tAtsCd")  private String tAtsCd;
		@JsonProperty("tAtsNm")  private String tAtsNm;

		private String areaCd;
		private String areaNm;
		private String signguCd;
		private String signguNm;

		@JsonProperty("rlteTatsCd")   private String rlteTatsCd;
		@JsonProperty("rlteTatsNm")   private String rlteTatsNm;
		@JsonProperty("rlteRegnCd")   private String rlteRegnCd;
		@JsonProperty("rlteRegnNm")   private String rlteRegnNm;
		@JsonProperty("rlteSignguCd") private String rlteSignguCd;
		@JsonProperty("rlteSignguNm") private String rlteSignguNm;

		@JsonProperty("rlteCtgryLclsNm") private String rlteCtgryLclsNm;
		@JsonProperty("rlteCtgryMclsNm") private String rlteCtgryMclsNm;
		@JsonProperty("rlteCtgrySclsNm") private String rlteCtgrySclsNm;

		private String rlteRank;
	}
}
