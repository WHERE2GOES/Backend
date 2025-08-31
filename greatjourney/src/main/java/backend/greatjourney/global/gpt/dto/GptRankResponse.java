package backend.greatjourney.global.gpt.dto;

import java.util.List;
import java.util.Map;

public record GptRankResponse(
	Map<String, Double> weights,       // 모델이 산출한 가중치(합=1)
	List<TopItem> top3,                // 상위 3개 (단일 응답 시 1개만 담김)
	List<String> extra_considerations  // 추가 고려사항
) {
	public record TopItem(
		String name,
		double score,
		Reasons reasons,
		WeightedBreakdown weighted,
		String image_url,      // 대표 이미지 URL
		String ai_summary      // ✅ AI 한 줄 추천 요약
	) {}

	// 평가 기준(5개)
	public record Reasons(
		double weather,    String weather_reason,
		double festival,   String festival_reason,
		double activity,   String activity_reason,
		double food,       String food_reason,
		double difficulty, String difficulty_reason
	) {}

	public record WeightedBreakdown(
		double weather,
		double festival,
		double activity,
		double food,
		double difficulty
	) {}
}
