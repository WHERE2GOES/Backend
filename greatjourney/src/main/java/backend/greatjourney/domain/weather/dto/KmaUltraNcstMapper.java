package backend.greatjourney.domain.weather.dto;

import backend.greatjourney.domain.weather.entity.PrecipitationType;

public class KmaUltraNcstMapper {

	private static Double toDouble(String v) {
		try { return v == null || v.isBlank() ? null : Double.valueOf(v); }
		catch (Exception e) { return null; }
	}

	private static Integer toInt(String v) {
		try { return v == null || v.isBlank() ? null : Integer.valueOf(Math.round(Float.parseFloat(v))); }
		catch (Exception e) { return null; }
	}

	/** KMA 응답 DTO → 한국어 DTO */
	public static UltraNcstKoreanDto toKorean(KmaUltraNcstDto src) {
		var body  = src.getResponse().getBody();
		var items = body.getItems() != null ? body.getItems().getItem() : java.util.List.<KmaUltraNcstDto.Item>of();

		// 대표 메타(첫 아이템 기준)
		String baseDate = items.isEmpty() ? null : items.get(0).getBaseDate();
		String baseTime = items.isEmpty() ? null : items.get(0).getBaseTime();
		Integer nx = items.isEmpty() ? null : items.get(0).getNx();
		Integer ny = items.isEmpty() ? null : items.get(0).getNy();

		UltraNcstKoreanDto.UltraNcstKoreanDtoBuilder builder = UltraNcstKoreanDto.builder()
			.baseDate(baseDate)
			.baseTime(baseTime)
			.X(nx)
			.Y(ny);

		for (var it : items) {
			String c = it.getCategory();
			String v = it.getObsrValue();

			switch (c) {
				case "PTY" -> builder.rainType(PrecipitationType.fromCode(v));
				case "T1H" -> builder.temperature(toDouble(v));
				case "REH" -> builder.humidity(toInt(v));
				case "WSD" -> builder.windSpeed(toDouble(v));
				case "VEC" -> builder.windDircetion(toInt(v));
				case "RN1" -> builder.RN1(toDouble(v));
				case "UUU" -> builder.UUU(toDouble(v));
				case "VVV" -> builder.VVV(toDouble(v));
			}

		}
		return builder.build();
	}
}

