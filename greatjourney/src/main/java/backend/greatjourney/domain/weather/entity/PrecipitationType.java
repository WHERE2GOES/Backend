package backend.greatjourney.domain.weather.entity;

import lombok.Getter;

@Getter
public enum PrecipitationType {
	맑음(0, "sunny"),
	비(1, "rain"),
	비눈(2, "rain/snow"),
	눈(3, "snow"),
	빗방울(5, "litte rain"),
	빗방울눈날림(6, "litte rain and snowy"),
	눈날림(7, "snow"),
	알수없음(-1, "unknown");

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
		return 알수없음;
	}
}
