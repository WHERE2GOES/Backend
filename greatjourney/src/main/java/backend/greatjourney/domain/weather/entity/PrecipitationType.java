package backend.greatjourney.domain.weather.entity;

import lombok.Getter;

@Getter
public enum PrecipitationType {
	sunny(0, "sunny"),
	rain(1, "rain"),
	rainAndSnow(2, "rain/snow"),
	snow(3, "snow"),
	rainDrop(5, "rain drop"),
	lightRainAndSnow(6, "little rain and snowy"),
	bigSnow(7, "snow"),
	unknown(-1, "unknown");

	private final int code;
	private final String desc;

	PrecipitationType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PrecipitationType fromCode(String v) {
		try {
			int code = Integer.parseInt(v);
			for (PrecipitationType p : values()) {
				if (p.code == code) return p;
			}
		} catch (Exception ignore) {}
		return unknown;
	}
}
