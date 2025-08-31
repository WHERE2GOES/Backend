package backend.greatjourney.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient tourClient(WebClient.Builder builder){
		DefaultUriBuilderFactory f =
			new DefaultUriBuilderFactory("https://apis.data.go.kr"); // ★ 도메인
		f.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // ★ 추가 인코딩 금지
		return WebClient.builder()
			.uriBuilderFactory(f)
			.build();
	}

	@Bean
	public WebClient kmaClient(WebClient.Builder builder) {
		DefaultUriBuilderFactory f =
			new DefaultUriBuilderFactory("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0");
		f.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
		return builder.uriBuilderFactory(f).build();
	}

}
