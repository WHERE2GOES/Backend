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

	// GptService 클래스 상단
	private static final Map<String, Integer> TRAIL_ID = Map.ofEntries(
		Map.entry("아라길", 1),
		Map.entry("한강종주길", 2),
		Map.entry("남한강길", 3),
		Map.entry("새재길", 4),
		Map.entry("낙동강길", 5),
		Map.entry("금강길", 6),
		Map.entry("영산강길", 7),
		Map.entry("북한강길", 8),
		Map.entry("섬진강길", 9),
		Map.entry("오천길", 10),
		Map.entry("동해안(강원)길", 11),
		Map.entry("동해안(경북)길", 12),
		Map.entry("제주환상길", 13)
	);
	private static final Map<Integer, String> ID_TO_NAME = TRAIL_ID.entrySet()
		.stream().collect(java.util.stream.Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));


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
				"https://ddo123.s3.ap-northeast-2.amazonaws.com/test_images/615416da-bbbd-44c6-9b1a-b342df2b4931_Group%202085667687.png",
				t.country(),
				t.id().toString()
			))
			.toList();
		return new GptTop3LiteResponse(items);
	}

	private String trailNameById(int trailId) {
		String name = ID_TO_NAME.get(trailId);
		if (name == null) {
			throw new IllegalArgumentException("Unknown trailId: " + trailId);
		}
		return name;
	}

	// 단일 코스 — 이름 + 이미지 1개 + 수치화된 모든 정보 반환
	public GptTrailFullResponse getTrailFullFromCache(String courseId) {

		String trailName = trailNameById(Integer.parseInt(courseId));

		var full = getTrailFromCache(trailName);
		var top = full.top3().stream()
			.filter(t -> equalsIgnoreCaseTrim(t.name(), trailName))
			.findFirst()
			.orElse(full.top3().get(0));

		return new GptTrailFullResponse(
			top.name(),
			"https://ddo123.s3.ap-northeast-2.amazonaws.com/test_images/615416da-bbbd-44c6-9b1a-b342df2b4931_Group%202085667687.png",
			top.country(),
			top.id().toString(),
			top.ai_summary(),
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
            당신은 한국 국토대장정 플래너입니다.
            반드시 JSON으로만 답하세요(추가 텍스트 금지).
            """;

			// computeStructured() 내 user 프롬프트 교체/추가 부분
			String user = """
			아래 13개 국토대장정 코스의 '현재 시기' 특성과 일반적 코스 특성을 바탕으로,
			평가 기준 5개(weather, festival, activity, food, difficulty)에 대한 '가중치'를 스스로 정하고(합=1),
			그 가중치로 상위 3곳을 선정하세요.
			
			응답의 각 top3 항목에는 다음 필드를 반드시 포함하세요:
			- name (후보명과 완전히 동일한 문자열 사용)
			- id (아래 매핑표의 정수 id, 절대 임의 생성 금지)
			- score, reasons{...}, weighted{...}, image_url, ai_summary, country(지역)
			
			[이름→id 매핑표]
			1: 아라길
			2: 한강종주길
			3: 남한강길
			4: 새재길
			5: 낙동강길
			6: 금강길
			7: 영산강길
			8: 북한강길
			9: 섬진강길
			10: 오천길
			11: 동해안(강원)길
			12: 동해안(경북)길
			13: 제주환상길
			
			지역(country)은 아래 중 하나로 정확히 기입:
			서울 인천 대전 대구 광주 부산 울산 세종특별자치시 경기도 강원특별자치도 충청북도 충청남도 경상북도 경상남도 전북특별자치도 전라남도 제주특별자치도
			
			후보 목록:
			- 아라길
			- 한강종주길
			- 남한강길
			- 새재길
			- 낙동강길
			- 금강길
			- 영산강길
			- 북한강길
			- 섬진강길
			- 오천길
			- 동해안(강원)길
			- 동해안(경북)길
			- 제주환상길
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
			"id", Map.of("type", "integer"),       // ✅ id 추가
			"name", Map.of("type", "string"),
			"score", Map.of("type", "number"),
			"country", Map.of("type", "string"),   // county → country 로 일관
			"reasons", Map.of(
				"type", "object",
				"additionalProperties", false,
				"required", List.of("weather","weather_reason","festival","festival_reason",
					"activity","activity_reason","food","food_reason",
					"difficulty","difficulty_reason"),
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
							// ✅ id, country를 required에 반드시 포함
							"required", List.of("id","name","score","country","reasons","weighted","image_url","ai_summary"),
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
