package backend.greatjourney.global.gpt.dto;

import java.util.List;
import java.util.Map;

public record GptTrailFullResponse(
	String name,
	String image_url,
	String country,
	String courseId,
	String ai_summary,                        // ✅ 한 줄 요약 포함
	Map<String, Double> weights,              // 모델 가중치
	double score,                             // 총점
	GptRankResponse.Reasons reasons,          // 기준별 점수 + 근거
	GptRankResponse.WeightedBreakdown weighted,// 기준별 가중 기여도
	List<String> extra_considerations         // 추가 고려사항
) {}
