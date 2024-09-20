package backend.greatjourney.domain.contents.service;

import backend.greatjourney.domain.contents.dto.LocationBasedListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ContentsService {

    @Value("${tour.api_key}")
    private String serviceKey;
    private final String BASE_URL = "http://apis.data.go.kr/B551011/KorService1";

    // API 호출을 위한 공통 메소드
    public Collection<LocationBasedListResponse> callApi(UriComponentsBuilder curUrl) throws IOException {
        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = curUrl.toUriString() + "&ServiceKey=" + serviceKey;
        log.info("Final URL: {}", finalUrl);

        // String을 URL 객체로 변환
        URL url = new URL(finalUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");


        try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
                        ? conn.getInputStream()
                        : conn.getErrorStream()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            log.info("sb: "+sb.toString());
            // JSON 응답을 WeatherResponse로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            LocationBasedListResponse weatherResponse = objectMapper.readValue(sb.toString(), LocationBasedListResponse.class);
//            LocationBasedListResponse weatherResponse = null;
//            return LocationBasedListResponse;
            // JSON 응답을 파싱하여 WeatherResponse로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(sb.toString());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<LocationBasedListResponse> contentsResponses = new ArrayList<>();
            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    // item을 WeatherResponse로 변환
                    LocationBasedListResponse weatherResponse = objectMapper.treeToValue(itemNode, LocationBasedListResponse.class);
                    contentsResponses.add(weatherResponse);
                }
            }

            return contentsResponses;

        } catch (IOException e) {
            System.err.println("Error reading the API response: " + e.getMessage());
            throw e;
        } finally {
            conn.disconnect();
        }
    }
}





