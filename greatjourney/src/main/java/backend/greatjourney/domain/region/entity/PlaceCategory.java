package backend.greatjourney.domain.region.entity;

// PlaceCategory.java
import lombok.Getter;
import java.util.Map;

@Getter
public enum PlaceCategory {
	FOOD("음식"),
	TOUR("관광지"),
	SLEEP("숙박");
	private final String kor;

	PlaceCategory(String kor) { this.kor = kor; }

	private static final Map<String, PlaceCategory> BY_SLUG = Map.of(
		"food", FOOD,
		"tour", TOUR,
		"sleep", SLEEP
	);

	public static PlaceCategory fromSlug(String slug) {
		PlaceCategory pc = BY_SLUG.get(slug.toLowerCase());
		if (pc == null) throw new IllegalArgumentException("Unsupported category: " + slug);
		return pc;
	}
}
