package backend.greatjourney.global.gpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.greatjourney.global.gpt.dto.GptRankResponse;

@Service
public class GptService {

	private final RestTemplate restTemplate;

	private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	private static final String MODEL   = "gpt-4o-mini";

	@Value("${gpt.key}")
	private String API_KEY;

	public GptService() {
		var factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(8000);
		factory.setReadTimeout(20000);
		this.restTemplate = new RestTemplate(factory);
	}
	// 기준 이름 고정 순서
	private static final List<String> CRITERIA = List.of(
		"accessibility", "scenery", "difficulty", "infra", "season"
	);

	// 베이스 가중치
	private static final Map<String, Double> BASE_WEIGHTS = Map.of(
		"accessibility", 0.20,
		"scenery",       0.20,
		"difficulty",    0.20,
		"infra",         0.20,
		"season",        0.20
	);


	private Map<String, Double> mixedRandomWeights(double lambda, Long seed) {
		java.util.Random r = (seed == null)
			? java.util.concurrent.ThreadLocalRandom.current()
			: new java.util.Random(seed);

		// 1) Dirichlet(α=1) ~ normalize(-ln(U))
		double[] e = new double[CRITERIA.size()];
		double sum = 0.0;
		for (int i = 0; i < e.length; i++) {
			double u = r.nextDouble();
			if (u <= 0.0) u = 1e-12; // 0 보호
			e[i] = -Math.log(u);     // Exp(1)
			sum += e[i];
		}

		// 2) base와 convex mix
		Map<String, Double> out = new java.util.LinkedHashMap<>();
		for (int i = 0; i < CRITERIA.size(); i++) {
			String k = CRITERIA.get(i);
			double rand = e[i] / sum;                // 0~1, Σ=1
			double base = BASE_WEIGHTS.get(k);       // 고정 베이스
			double w = (1.0 - lambda) * base + lambda * rand;
			out.put(k, w);
		}

		// 3) 수치 오차 방지: 마지막에 한 번 더 정규화
		double s = out.values().stream().mapToDouble(Double::doubleValue).sum();
		if (Math.abs(s - 1.0) > 1e-9) {
			for (String k : CRITERIA) out.put(k, out.get(k) / s);
		}
		return out;
	}



	@Cacheable(
		cacheNames = "gptBikeTrails",
		key = "'rankBikeTrails:v3'",
		cacheManager = "cacheManager",
		unless = "#result == null || #result.top3() == null || #result.top3().isEmpty()"
	)
	public GptRankResponse rankBikeTrails() {
		// 안정적인 기본값 (temperature 낮게)
		return computeRankBikeTrails(BASE_WEIGHTS, 0.2);
	}

	/**
	 * 매번 가중치 랜덤(λ로 강도 조절) + 캐시에 덮어쓰기
	 * - lambda: 0.0(고정) ~ 1.0(완전 랜덤)
	 * - seed: 재현 가능한 랜덤 원하면 사용(없으면 null)
	 */
	@CachePut(
		cacheNames = "gptBikeTrails",
		cacheManager = "cacheManager",
		key = "'rankBikeTrails:v3'"
	)
	public GptRankResponse refreshBikeTrailsRandom(double lambda, Long seed) {
		if (lambda < 0.0) lambda = 0.0;
		if (lambda > 1.0) lambda = 1.0;
		Map<String, Double> w = mixedRandomWeights(lambda, seed);
		// 변화 폭을 키우고 싶으면 temperature도 약간 올리기
		return computeRankBikeTrails(w, lambda >= 0.5 ? 0.7 : 0.4);
	}


	@CacheEvict(
		cacheNames = "gptBikeTrails",
		cacheManager = "cacheManager",
		key = "'rankBikeTrails:v3'"
	)
	public void evictBikeTrails() {
		// no-op
	}


	private GptRankResponse computeRankBikeTrails(Map<String, Double> weights, double temperature) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(API_KEY);


		String system = """
        당신은 한국 자전거 여행 플래너입니다.
        반드시 JSON으로만 답하세요(추가 텍스트 금지).
        점수는 0~10 사이 실수로 평가하고, 최종 score는 지정된 가중치와의 곱의 합(가중합)으로 계산하세요.
        응답에는 사용한 가중치(weights)와 각 기준별 가중 기여도(weighted)를 포함하세요.
        """;

		String user = """
        아래 13개 자전거길 중 우선순위 상위 3곳을 선정해 주세요.
        각 항목별로 기준별 점수(0~10)와 한 줄 근거를 제시하고,
        지정된 가중치로 가중합(score)을 계산하세요.
        마지막으로 '추가로 고려하면 좋은 점'을 3~6개 bullet로 주세요.

        후보 목록:
        - 아라자전거길
        - 한강종주자전거길
        - 남한강자전거길
        - 새재자전거길
        - 낙동강자전거길
        - 금강자전거길
        - 영산강자전거길
        - 북한강자전거길
        - 섬진강자전거길
        - 오천자전거길
        - 동해안(강원)자전거길
        - 동해안(경북)자전거길
        - 제주환상자전거길

        평가 기준과 가중치:
        - accessibility(0.25): 대중교통 접근성/회귀 편의
        - scenery(0.30): 수변/산/바다 뷰, 하이라이트 포인트 밀도
        - difficulty(0.10): 초중급 적합성, 고저/바람/노면
        - infra(0.20): 휴게/화장실/급수/정비, 표지/안전
        - season(0.15): 계절/기상 민감도(폭염, 미세먼지, 결빙, 해풍 등)

        응답 스키마(이 구조와 키를 정확히 따르세요):
        {
          "weights": { "accessibility": 0.25, "scenery": 0.30, "difficulty": 0.10, "infra": 0.20, "season": 0.15 },
          "top3": [
            {
              "name": "코스명",
              "score": 0.0,
              "reasons": {
                "accessibility": 0.0, "accessibility_reason": "한 줄",
                "scenery": 0.0,       "scenery_reason": "한 줄",
                "difficulty": 0.0,    "difficulty_reason": "한 줄",
                "infra": 0.0,         "infra_reason": "한 줄",
                "season": 0.0,        "season_reason": "한 줄"
              },
              "weighted": {
                "accessibility": 0.0,
                "scenery": 0.0,
                "difficulty": 0.0,
                "infra": 0.0,
                "season": 0.0
              }
            }
          ],
          "extra_considerations": ["문장", "문장"]
        }

        주의: JSON 외 텍스트 금지.
        """;

		// === 4) 요청 바디 ===
		Map<String, Object> body = new HashMap<>();
		body.put("model", MODEL);
		body.put("temperature", temperature);
		body.put("messages", List.of(
			Map.of("role", "system", "content", system),
			Map.of("role", "user",   "content", injectWeightsAsJson(user, weights))
		));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
			Map<String, Object> respBody = response.getBody();
			if (respBody == null) throw new IllegalStateException("Empty response");

			List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
			if (choices == null || choices.isEmpty()) throw new IllegalStateException("No choices");

			Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
			String content = (String) message.get("content");
			if (content == null || content.isBlank()) throw new IllegalStateException("Empty content");

			ObjectMapper om = new ObjectMapper();
			return om.readValue(content, GptRankResponse.class);

		} catch (Exception e) {
			return new GptRankResponse(
				weights,
				List.of(),
				List.of("네트워크/모델 오류 발생: " + e.getMessage())
			);
		}
	}

	private String injectWeightsAsJson(String prompt, Map<String, Double> weights) {
		try {
			ObjectMapper om = new ObjectMapper();
			return prompt + "\n\n가중치(JSON): " + om.writeValueAsString(weights);
		} catch (Exception e) {
			return prompt + "\n\n가중치(JSON): " + weights.toString(); // fallback
		}
	}
}
