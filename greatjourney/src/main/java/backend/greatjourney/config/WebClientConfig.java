package backend.greatjourney.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient tourClient(WebClient.Builder builder){
		return builder.baseUrl("https://apis.data.go.kr")
			.build();
	}
}
