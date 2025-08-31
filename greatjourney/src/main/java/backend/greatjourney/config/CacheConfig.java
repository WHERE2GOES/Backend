package backend.greatjourney.config;

// CacheConfig.java
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import backend.greatjourney.global.gpt.dto.GptRankResponse;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	@Primary
	public CacheManager cacheManager(RedisConnectionFactory cf, ObjectMapper om) {

		// 기본 캐시: 기존 설정 유지(6h, GenericJackson2JsonRedisSerializer 사용)
		var defaultValueSer = RedisSerializationContext.SerializationPair
			.fromSerializer(new GenericJackson2JsonRedisSerializer(om));

		var base = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(defaultValueSer)
			.disableCachingNullValues()
			.entryTtl(Duration.ofHours(6));

		// gptBikeTrails 전용: 구체 타입 직렬화기 + TTL 없음(요청 있을 때만 갱신)
		var gptSer = new Jackson2JsonRedisSerializer<>(GptRankResponse.class); // 또는 reader/writer 생성자
		var gptCfg = base
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(gptSer))
			.entryTtl(Duration.ZERO);

		Map<String, RedisCacheConfiguration> perCache = new HashMap<>();
		perCache.put("relatedPlaces", base.entryTtl(Duration.ofHours(6)));
		perCache.put("gptBikeTrails", gptCfg);

		return RedisCacheManager.builder(cf)
			.cacheDefaults(base)
			.withInitialCacheConfigurations(perCache)
			.build();
	}

}
