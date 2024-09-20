package backend.greatjourney.domain.weather.service;

import backend.greatjourney.domain.weather.dto.WeatherRequest;
import backend.greatjourney.domain.weather.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j

public class WeatherService {

    @Value("${weather.api_key}")
    private String serviceKey;
    public List<WeatherResponse> getWeather(WeatherRequest request) throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");

        // 각 파라미터를 URL에 동적으로 추가
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(request.getNumOfRows(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(request.getPageNo(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(request.getBase_date(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(request.getBase_time(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(request.getLocationX(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(request.getLocationY(), "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        System.out.println("Response code: " + conn.getResponseCode());
        System.out.println("Response message: " + serviceKey);
        // try-with-resources로 BufferedReader 처리
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
                        ? conn.getInputStream()
                        : conn.getErrorStream()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            System.out.println(sb.toString());
            // JSON 응답을 WeatherResponse로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
////            WeatherResponse weatherResponse = objectMapper.readValue(sb.toString(), WeatherResponse.class);
//            WeatherResponse weatherResponse = null;
//            return weatherResponse;
            // JSON 응답을 파싱하여 WeatherResponse로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(sb.toString());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            List<WeatherResponse> weatherResponses = new ArrayList<>();
            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    // item을 WeatherResponse로 변환
                    WeatherResponse weatherResponse = objectMapper.treeToValue(itemNode, WeatherResponse.class);
                    weatherResponses.add(weatherResponse);
                }
            }

            return weatherResponses;

        } catch (IOException e) {
            System.err.println("Error reading the API response: " + e.getMessage());
            throw e;
        } finally {
            conn.disconnect();
        }
    }


}
