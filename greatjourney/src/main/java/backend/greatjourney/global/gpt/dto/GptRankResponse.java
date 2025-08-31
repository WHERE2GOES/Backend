// GptRankResponse.java
package backend.greatjourney.global.gpt.dto;

import java.util.List;
import java.util.Map;

public record GptRankResponse(
	Map<String, Double> weights,       // 사용한 가중치 그대로 반환
	List<TopItem> top3,                // 상위 3개
	List<String> extra_considerations  // 추가 고려사항
) {
	public record TopItem(
		String name,
		double score,                   // 가중합(= Σ weight * criterion_score)
		Reasons reasons,                // 원 점수(0~10) + 한 줄 근거
		WeightedBreakdown weighted      // 각 기준별 가중 기여도
	) {}

	public record Reasons(
		double accessibility,  String accessibility_reason,
		double scenery,        String scenery_reason,
		double difficulty,     String difficulty_reason,
		double infra,          String infra_reason,
		double season,         String season_reason
	) {}

	public record WeightedBreakdown(
		double accessibility,  // weight.accessibility * reasons.accessibility
		double scenery,
		double difficulty,
		double infra,
		double season
	) {}
}
