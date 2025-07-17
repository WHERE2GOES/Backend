package backend.greatjourney.domain.user.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoOAuthProperties {
	private String clientId;
	private String clientSecret;
}
