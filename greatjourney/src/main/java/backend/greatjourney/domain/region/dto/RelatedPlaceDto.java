package backend.greatjourney.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatedPlaceDto {
	private String name;        // rlteTatsNm
	private String rank;        // rlteRank
	private String siName;      // rlteRegnNm
	private String sigunguName; // rlteSignguNm
	private String lcls;        // rlteCtgryLclsNm
	private String mcls;        // rlteCtgryMclsNm
	private String scls;        // rlteCtgrySclsNm
}