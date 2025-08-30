package backend.greatjourney.config.client;

import backend.greatjourney.domain.region.dto.RegionApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class RegionApiClient {

	private final WebClient webClient;

	@Value("${tour.key}")
	private String serviceKey;
	@Value("${tour.end}")
	private String paths;

	public RegionApiResponse callAreaBasedList(String baseYm, String areaCd, String signguCd,
		int pageNo, int numOfRows) {

		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path(paths)
				.queryParam("serviceKey", serviceKey)
				.queryParam("pageNo", pageNo)
				.queryParam("numOfRows", numOfRows)
				.queryParam("MobileOS", "AND")
				.queryParam("MobileApp", "어디로")
				.queryParam("baseYm", baseYm)
				.queryParam("areaCd", areaCd)
				.queryParam("signguCd", signguCd)
				.queryParam("_type", "json")
				.build())
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(RegionApiResponse.class)
			.block();
	}
}
