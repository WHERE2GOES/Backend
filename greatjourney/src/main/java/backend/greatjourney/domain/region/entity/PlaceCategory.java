package backend.greatjourney.domain.region.entity;

// PlaceCategory.java
import lombok.Getter;
import java.util.Map;


@Getter
public enum PlaceCategory {
	FOOD("음식", null),          // lcls = 음식
	TOUR("관광지", null),        // lcls = 관광지
	SLEEP("숙박", null),         // lcls = 숙박
	PHOTO("관광지", "문화관광");  // lcls = 관광지 AND mcls = 문화관광  ← 신규

	private final String lclsKor;     // 대분류명(lcls)
	private final String mclsKor;     // (옵션) 중분류명(mcls)

	PlaceCategory(String lclsKor, String mclsKor) {
		this.lclsKor = lclsKor;
		this.mclsKor = mclsKor;
	}

	// slug 매핑 (원하는 별칭 더 추가 가능)
	private static final Map<String, PlaceCategory> BY_SLUG = Map.of(
		"food",  FOOD,
		"tour",  TOUR,
		"sleep", SLEEP,
		"photo", PHOTO,
		"photospot", PHOTO
	);

	public static PlaceCategory fromSlug(String slug) {
		PlaceCategory pc = BY_SLUG.get(slug.toLowerCase());
		if (pc == null) throw new IllegalArgumentException("Unsupported category: " + slug);
		return pc;
	}

	/**
	 * 해당 카테고리가 아이템과 매칭되는지 여부
	 * - mclsKor가 없으면 lcls만 비교
	 * - mclsKor가 있으면 lcls AND mcls 모두 비교
	 */
	public boolean matches(backend.greatjourney.domain.region.dto.RegionApiResponse.Item i) {
		boolean lclsOk = this.lclsKor.equals(i.getRlteCtgryLclsNm());
		if (!lclsOk) return false;
		if (this.mclsKor == null) return true;
		return this.mclsKor.equals(i.getRlteCtgryMclsNm());
	}
}

