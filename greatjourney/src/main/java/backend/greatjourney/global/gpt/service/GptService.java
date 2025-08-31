package backend.greatjourney.global.gpt.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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
import backend.greatjourney.global.gpt.dto.GptTop3LiteResponse;
import backend.greatjourney.global.gpt.dto.GptTrailFullResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GptService {

	private final RestTemplate restTemplate;
	private final CacheManager cacheManager;

	private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
	private static final String MODEL    = "gpt-4o-mini"; // JSON Schema(strict) 지원

	private static final String CACHE_NAME = "gptBikeTrails";
	private static final String KEY_TOP3   = "rankBikeTrails:v3";
	private static final String KEY_PREFIX = "rankBikeTrail:v3:"; // 단일 코스 키 프리픽스

	@Value("${gpt.key}")
	private String API_KEY;

	public GptService(CacheManager cacheManager) {
		var factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(8000);
		factory.setReadTimeout(20000);
		this.restTemplate = new RestTemplate(factory);
		this.cacheManager = cacheManager;
	}

	// ========= 유틸 =========
	private String norm(String s) {
		if (s == null) return "";
		String n = Normalizer.normalize(s, Normalizer.Form.NFKC)
			.trim()
			.toLowerCase(Locale.ROOT)
			.replaceAll("\\s+", " ");
		return n;
	}

	private boolean equalsIgnoreCaseTrim(String a, String b) {
		if (a == null || b == null) return false;
		return a.trim().equalsIgnoreCase(b.trim());
	}

	private String safeImageUrl(String name, String url) {
		if (url != null && !url.isBlank() && url.startsWith("http")) return url;
		String q = URLEncoder.encode(name + " 자전거길", StandardCharsets.UTF_8);
		return "https://www.google.com/search?tbm=isch&q=" + q;
	}

	// =======================
	// Top3 묶음 (계산 + 캐시)
	// =======================
	@Cacheable(
		cacheNames = CACHE_NAME,
		key = "'" + KEY_TOP3 + "'",
		cacheManager = "cacheManager",
		unless = "#result == null || #result.top3() == null || #result.top3().isEmpty()"
	)
	public GptRankResponse rankBikeTrails() {
		GptRankResponse r = computeStructured(0.2);
		if (r != null && r.top3() != null && !r.top3().isEmpty()) {
			seedSinglesFromTop3(r);
			return r;
		}
		// 안전 폴백(혹시라도): 빈 구조
		return new GptRankResponse(Map.of(), List.of(), List.of("모델 응답 비정상"));
	}

	@CachePut(
		cacheNames = CACHE_NAME,
		cacheManager = "cacheManager",
		key = "'" + KEY_TOP3 + "'"
	)
	public GptRankResponse refreshBikeTrails() {
		GptRankResponse r = computeStructured(0.4);
		if (r != null && r.top3() != null && !r.top3().isEmpty()) {
			seedSinglesFromTop3(r);
			return r;
		}
		return new GptRankResponse(Map.of(), List.of(), List.of("모델 응답 비정상"));
	}

	@CacheEvict(
		cacheNames = CACHE_NAME,
		cacheManager = "cacheManager",
		key = "'" + KEY_TOP3 + "'"
	)
	public void evictBikeTrails() { }

	// Top3 → 단일 키 즉시 시드
	private void seedSinglesFromTop3(GptRankResponse top3) {
		var cache = cacheManager.getCache(CACHE_NAME);
		if (cache == null) return;
		for (var item : top3.top3()) {
			String singleKey = KEY_PREFIX + norm(item.name());
			GptRankResponse single = new GptRankResponse(top3.weights(), List.of(item), top3.extra_considerations());
			cache.put(singleKey, single);
		}
	}

	// Top3 캐시 먼저 읽고, 없으면 계산해서 put
	public GptRankResponse getTop3CachedFirst() {
		var cache = cacheManager.getCache(CACHE_NAME);
		if (cache != null) {
			GptRankResponse cached = cache.get(KEY_TOP3, GptRankResponse.class);
			if (cached != null && cached.top3() != null && !cached.top3().isEmpty()) {
				return cached; // ✅ 캐시 히트 → 절대 OpenAI 안감
			}
		}
		// 캐시 미스 → 계산
		GptRankResponse fresh = computeStructured(0.2);
		if (fresh == null || fresh.top3() == null || fresh.top3().isEmpty()) {
			// 안전 폴백
			return new GptRankResponse(Map.of(), List.of(), List.of("모델 응답 비정상"));
		}
		// 직접 put + 단일 키들도 시드
		if (cache != null) {
			cache.put(KEY_TOP3, fresh);
			seedSinglesFromTop3(fresh);
		}
		return fresh;
	}

	// LITE 응답도 캐시 우선 경로 사용하도록
	public GptTop3LiteResponse getTop3LiteCachedFirst() {
		return toLite(getTop3CachedFirst());
	}


	// =======================
	// 단일 코스 — 캐시에서만 반환 (+없으면 Top3로부터 즉시 시드)
	// =======================
	public GptRankResponse getTrailFromCache(String trailName) {
		if (trailName == null || trailName.isBlank()) {
			throw new IllegalArgumentException("trailName must not be blank");
		}
		var cache = cacheManager.getCache(CACHE_NAME);
		if (cache == null) {
			throw new IllegalStateException("Cache '" + CACHE_NAME + "' not configured");
		}

		String key = KEY_PREFIX + norm(trailName);
		GptRankResponse res = cache.get(key, GptRankResponse.class);
		if (res != null && res.top3() != null && !res.top3().isEmpty()) {
			return res;
		}

		// 단일 키 없으면 Top3에서 파생해 즉시 시드
		GptRankResponse top3 = cache.get(KEY_TOP3, GptRankResponse.class);
		if (top3 != null && top3.top3() != null && !top3.top3().isEmpty()) {
			var opt = top3.top3().stream()
				.filter(t -> equalsIgnoreCaseTrim(t.name(), trailName))
				.findFirst();
			if (opt.isPresent()) {
				GptRankResponse single = new GptRankResponse(top3.weights(), List.of(opt.get()), top3.extra_considerations());
				cache.put(key, single);
				return single;
			}
		}

		throw new IllegalStateException("No cached result for trail: " + trailName);
	}

	// 목록 요약(LITE)
	public GptTop3LiteResponse getTop3Lite() {
		return toLite(rankBikeTrails());
	}

	public GptTop3LiteResponse refreshAndGetLite() {
		return toLite(refreshBikeTrails());
	}

	private GptTop3LiteResponse toLite(GptRankResponse full) {
		var items = full.top3().stream()
			.map(t -> new GptTop3LiteResponse.Item(
				t.name(),
				safeImageUrl(t.name(), t.image_url())
			))
			.toList();
		return new GptTop3LiteResponse(items);
	}

	// 단일 코스 — 이름 + 이미지 1개 + 수치화된 모든 정보 반환
	public GptTrailFullResponse getTrailFullFromCache(String trailName) {
		var full = getTrailFromCache(trailName);
		var top = full.top3().stream()
			.filter(t -> equalsIgnoreCaseTrim(t.name(), trailName))
			.findFirst()
			.orElse(full.top3().get(0));

		return new GptTrailFullResponse(
			top.name(),
			safeImageUrl(top.name(), top.image_url()),
			top.ai_summary(),               // ✅ 한 줄 추천 요약
			full.weights(),
			top.score(),
			top.reasons(),
			top.weighted(),
			full.extra_considerations()
		);
	}

	// =======================
	// OpenAI 호출부 — Chat Completions + JSON Schema(strict)
	// =======================
	private GptRankResponse computeStructured(double temperature) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(API_KEY);

			String system = """
            당신은 한국 자전거 여행 플래너입니다.
            반드시 JSON으로만 답하세요(추가 텍스트 금지).
            """;

			String user = """
            아래 13개 자전거길의 '현재 시기' 특성과 일반적 코스 특성을 바탕으로,
            평가 기준 5개(weather, festival, activity, food, difficulty)에 대한 '가중치'를 스스로 정하고(합=1),
            그 가중치로 상위 3곳을 선정하세요.
            응답에는 각 코스의 image_url(공개 https)과 ai_summary(25자 내외 한국어 한 문장)도 포함하세요.

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
            """;

			// === JSON Schema(strict) ===
			Map<String, Object> schema = buildJsonSchema();

			Map<String, Object> body = new HashMap<>();
			body.put("model", MODEL);
			body.put("temperature", temperature);
			body.put("response_format", Map.of(
				"type", "json_schema",
				"json_schema", schema
			));
			body.put("messages", List.of(
				Map.of("role", "system", "content", system),
				Map.of("role", "user",   "content", user)
			));

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
			ResponseEntity<Map> response = restTemplate.postForEntity(CHAT_URL, request, Map.class);

			Map<String, Object> respBody = response.getBody();
			if (respBody == null) throw new IllegalStateException("Empty response");

			List<Map<String, Object>> choices = (List<Map<String, Object>>) respBody.get("choices");
			if (choices == null || choices.isEmpty()) throw new IllegalStateException("No choices");

			Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
			String content = (String) message.get("content");
			if (content == null || content.isBlank()) throw new IllegalStateException("Empty content");

			// 디버깅용 로그(필요시 주석 해제)
			// log.info("LLM JSON: {}", content);

			ObjectMapper om = new ObjectMapper();
			return om.readValue(content, GptRankResponse.class);

		} catch (Exception e) {
			log.warn("computeStructured() failed: {}", e.getMessage());
			return null;
		}
	}

	private Map<String, Object> buildJsonSchema() {
		// JSON Schema for GptRankResponse with strict true
		Map<String, Object> weightsProps = Map.of(
			"weather", Map.of("type", "number"),
			"festival", Map.of("type", "number"),
			"activity", Map.of("type", "number"),
			"food", Map.of("type", "number"),
			"difficulty", Map.of("type", "number")
		);

		Map<String, Object> reasonsProps = Map.of(
			"weather", Map.of("type", "number"),
			"weather_reason", Map.of("type", "string"),
			"festival", Map.of("type", "number"),
			"festival_reason", Map.of("type", "string"),
			"activity", Map.of("type", "number"),
			"activity_reason", Map.of("type", "string"),
			"food", Map.of("type", "number"),
			"food_reason", Map.of("type", "string"),
			"difficulty", Map.of("type", "number"),
			"difficulty_reason", Map.of("type", "string")
		);

		Map<String, Object> weightedProps = Map.of(
			"weather", Map.of("type", "number"),
			"festival", Map.of("type", "number"),
			"activity", Map.of("type", "number"),
			"food", Map.of("type", "number"),
			"difficulty", Map.of("type", "number")
		);

		Map<String, Object> topItemProps = Map.of(
			"name", Map.of("type", "string"),
			"score", Map.of("type", "number"),
			"reasons", Map.of(
				"type", "object",
				"additionalProperties", false,
				"required", List.of("weather","weather_reason","festival","festival_reason","activity","activity_reason","food","food_reason","difficulty","difficulty_reason"),
				"properties", reasonsProps
			),
			"weighted", Map.of(
				"type", "object",
				"additionalProperties", false,
				"required", List.of("weather","festival","activity","food","difficulty"),
				"properties", weightedProps
			),
			"image_url", Map.of("type", "string"),
			"ai_summary", Map.of("type", "string")
		);

		Map<String, Object> schema = Map.of(
			"name", "BikeTop3",
			"strict", true,
			"schema", Map.of(
				"type", "object",
				"additionalProperties", false,
				"required", List.of("weights", "top3", "extra_considerations"),
				"properties", Map.of(
					"weights", Map.of(
						"type", "object",
						"additionalProperties", false,
						"required", List.of("weather","festival","activity","food","difficulty"),
						"properties", weightsProps
					),
					"top3", Map.of(
						"type", "array",
						"minItems", 1,
						"maxItems", 3,
						"items", Map.of(
							"type", "object",
							"additionalProperties", false,
							"required", List.of("name","score","reasons","weighted","image_url","ai_summary"),
							"properties", topItemProps
						)
					),
					"extra_considerations", Map.of(
						"type", "array",
						"items", Map.of("type", "string")
					)
				)
			)
		);

		return schema;
	}
}
