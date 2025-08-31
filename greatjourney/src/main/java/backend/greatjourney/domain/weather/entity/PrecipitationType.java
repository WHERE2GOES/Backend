package backend.greatjourney.domain.weather.entity;

import lombok.Getter;

@Getter
public enum PrecipitationType {
	맑음(0, "맑음"),
	비(1, "비"),
	비눈(2, "비/눈"),
	눈(3, "눈"),
	빗방울(5, "빗방울"),
	빗방울눈날림(6, "빗방울눈날림"),
	눈날림(7, "눈날림"),
	알수없음(-1, "알수없음");

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
