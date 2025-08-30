package backend.greatjourney.config;

// CacheConfig.java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory cf, ObjectMapper om) {
		RedisSerializationContext.SerializationPair<Object> valueSerializer =
			RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer(om));

		RedisCacheConfiguration base = RedisCacheConfiguration.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(valueSerializer)
			.disableCachingNullValues()
			.entryTtl(Duration.ofHours(6));

		return RedisCacheManager.builder(cf)
			.cacheDefaults(base)
			.withCacheConfiguration("relatedPlaces", base.entryTtl(Duration.ofHours(6)))
			.build();
	}
}
