package backend.greatjourney.domain.weather.service;

import backend.greatjourney.domain.weather.dto.LocationDTO;
import backend.greatjourney.domain.weather.dto.WeatherRequest;
import backend.greatjourney.domain.weather.dto.WeatherResponse;
import backend.greatjourney.domain.weather.entity.WeatherLocationEntity;
import backend.greatjourney.domain.weather.repository.WeatherLocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class WeatherService {

    private final WeatherLocationRepository weatherLocationRepository;

    // Base_time 리스트 (불변 리스트로 선언)
    private static final List<String> BASE_TIMES = Collections.unmodifiableList(List.of(
            "0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"
    ));

    public static String[] getNearestBaseDateTime() {
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // "HHmm" 형태로 현재 시간을 포맷팅
        String formattedNow = now.format(DateTimeFormatter.ofPattern("HHmm"));
        int currentTime = Integer.parseInt(formattedNow);

        // 날짜 포맷 (yyyyMMdd)
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 만약 현재 시간이 0200 이전이라면 전날 날짜와 2300 시간 반환
        if (currentTime < 200) {
            // 전날 날짜 계산
            String previousDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return new String[] { previousDate, "2300" };
        }

        // 그 외의 경우, 가장 가까운 Base_time 찾기
        String closestBaseTime = findClosestTime(BASE_TIMES, currentTime);

        // 날짜와 시간을 배열로 반환
        return new String[] { formattedDate, closestBaseTime };
    }

    // 가장 가까운 시간 찾는 메서드
    private static String findClosestTime(List<String> timeList, int currentTime) {
        String closestTime = timeList.get(0);
        int minDifference = Math.abs(currentTime - Integer.parseInt(closestTime));

        for (String time : timeList) {
            int baseTime = Integer.parseInt(time);
            int difference = Math.abs(currentTime - baseTime);

            if (difference < minDifference) {
                minDifference = difference;
                closestTime = time;
            }
        }
        return closestTime;
    }



    @Value("${weather.api_key}")
    private String serviceKey;

    //쿼리에 따라서 날씨 정보를 가져오는 것
//    public List<WeatherResponse> getWeather(String step1,String step2,String step3,String pageNo, String numOfRows) throws IOException {
//
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst");
//
//        LocationDTO locatedto = changeLocation(step1,step2,step3);
//
//        if(locatedto==null) {
//            return null;
//        }
//
//        String[] dateData = getNearestBaseDateTime();
//        // 각 파라미터를 URL에 동적으로 추가
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey);
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(dateData[0], "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(dateData[1], "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(locatedto.getX(), "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(locatedto.getY(), "UTF-8"));
//
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//
//        System.out.println("Response code: " + conn.getResponseCode());
////        System.out.println("Response message: " + serviceKey);
//        // try-with-resources로 BufferedReader 처리
//        try (BufferedReader rd = new BufferedReader(new InputStreamReader(
//                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
//                        ? conn.getInputStream()
//                        : conn.getErrorStream()))) {
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//                System.out.println(line);
//            }
//
//            // JSON 응답을 WeatherResponse로 변환
////            ObjectMapper objectMapper = new ObjectMapper();
//////            WeatherResponse weatherResponse = objectMapper.readValue(sb.toString(), WeatherResponse.class);
////            WeatherResponse weatherResponse = null;
////            return weatherResponse;
//            // JSON 응답을 파싱하여 WeatherResponse로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(sb.toString());
//            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
//
//            List<WeatherResponse> weatherResponses = new ArrayList<>();
//            if (itemsNode.isArray()) {
//                for (JsonNode itemNode : itemsNode) {
//                    // item을 WeatherResponse로 변환
//                    WeatherResponse weatherResponse = objectMapper.treeToValue(itemNode, WeatherResponse.class);
//                    weatherResponses.add(weatherResponse);
//                }
//            }
//
//            return weatherResponses;
//
//        } catch (IOException e) {
//            System.err.println("Error reading the API response: " + e.getMessage());
//            throw e;
//        } finally {
//            conn.disconnect();
//        }
//    }
    public WeatherResponse getWeather(String step1, String step2, String step3, String pageNo, String numOfRows) throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");

        LocationDTO locationDTO = changeLocation(step1, step2, step3);

        if (locationDTO == null) {
            return null; // 위치 정보를 찾지 못하면 null 반환
        }

        String[] dateData = getNearestBaseDateTime(); // 날짜와 시간 가져오기

        // 각 파라미터를 URL에 동적으로 추가
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(dateData[0], "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(dateData[1], "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(locationDTO.getX(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(locationDTO.getY(), "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

//        System.out.println("Response code: " + conn.getResponseCode());
//        System.out.println("Response message: " + conn.getResponseMessage());
//        System.out.println("Date Data" + Arrays.toString(dateData));

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

//            System.out.println(sb.toString());
            // JSON 응답을 파싱하여 WeatherResponseDTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(sb.toString());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            WeatherResponse weatherResponseDTO = new WeatherResponse(); // 단일 DTO 객체 생성

            if (itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {

                    String category = itemNode.path("category").asText();
                    String fcstValue = itemNode.path("fcstValue").asText();

                    // 각 category에 따라 값을 DTO에 세팅
                    switch (category) {
                        case "POP": // 강수확률
                            weatherResponseDTO.setPrecipitationProbability(fcstValue + "%");
                            break;
                        case "REH": // 습도
                            weatherResponseDTO.setHumidity(fcstValue + "%");
                            break;
                        case "PTY": // 강수형태
                            weatherResponseDTO.setPrecipitationType(mapPrecipitationType(fcstValue));
                            break;
                        case "TMP": // 기온
                            weatherResponseDTO.setTemperature(fcstValue + "℃");
                            break;
                        case "WSD": // 풍속
                            weatherResponseDTO.setWindSpeed(fcstValue + "m/s");
                            break;
                        case "SKY": // 하늘상태
                            weatherResponseDTO.setSkyCondition(mapSkyCondition(fcstValue));
                            break;
                        // 필요한 경우 다른 카테고리 추가 가능

                    }
                }
            }

            return weatherResponseDTO; // 단일 WeatherResponseDTO 객체 반환

        } catch (IOException e) {
            System.err.println("Error reading the API response: " + e.getMessage());
            throw e;
        } finally {
            conn.disconnect();
        }
    }

    // 강수형태(PTY) 코드 매핑
    private static String mapPrecipitationType(String value) {
        switch (value) {
            case "0":
                return "없음";
            case "1":
                return "비";
            case "2":
                return "비/눈";
            case "3":
                return "눈";
            case "4":
                return "소나기";
            case "5":
                return "빗방울";
            case "6":
                return "빗방울/눈날림";
            case "7":
                return "눈날림";
            default:
                return "알 수 없음";
        }
    }

    // 하늘상태(SKY) 코드 매핑
    private static String mapSkyCondition(String value) {
        switch (value) {
            case "1":
                return "맑음";
            case "3":
                return "구름많음";
            case "4":
                return "흐림";
            default:
                return "알 수 없음";
        }
    }

    public LocationDTO changeLocation(String step1, String step2, String step3){
       List<WeatherLocationEntity> entities = weatherLocationRepository.findByStep1(step1);

        if (entities == null || entities.isEmpty()) {
            return null;
        }

    // step2와 step3에 대해 확인할 조건


        List<WeatherLocationEntity> step2Results = new ArrayList<>();



// step2 조건 확인
        for (WeatherLocationEntity entity : entities) {
            if (entity.getStep2().equals(step2)) {
                step2Results.add(entity);
            }
        }

// step3 조건 확인
        List<WeatherLocationEntity> step3Results = new ArrayList<>();
        for (WeatherLocationEntity entity : step2Results) {
            if (entity.getStep3().equals(step3)) {
                step3Results.add(entity);
            }
        }

// 결과 반환
        if (!step3Results.isEmpty()) {
            return  LocationDTO.builder()
                    .Hour1(step3Results.get(0).getHour1())
                    .X(step3Results.get(0).getX())
                    .Y(step3Results.get(0).getY())
                    .Step1(step3Results.get(0).getStep1())
                    .Step2(step3Results.get(0).getStep2())
                    .Step3(step3Results.get(0).getStep3())
                    .Hour2(step3Results.get(0).getHour2())
                    .Minute1(step3Results.get(0).getMinute1())
                    .Minute2(step3Results.get(0).getMinute2())
                    .Second1(step3Results.get(0).getSecond1())
                    .Second2(step3Results.get(0).getSecond2())
                    .build(); // step3 조건을 만족하는 결과 반환
        } else if (!step2Results.isEmpty()) {
            return  LocationDTO.builder()
                    .Hour1(step2Results.get(0).getHour1())
                    .X(step2Results.get(0).getX())
                    .Y(step2Results.get(0).getY())
                    .Step1(step2Results.get(0).getStep1())
                    .Step2(step2Results.get(0).getStep2())
                    .Step3(step2Results.get(0).getStep3())
                    .Hour2(step2Results.get(0).getHour2())
                    .Minute1(step2Results.get(0).getMinute1())
                    .Minute2(step2Results.get(0).getMinute2())
                    .Second1(step2Results.get(0).getSecond1())
                    .Second2(step2Results.get(0).getSecond2())
                    .build();
        }  else  {
            return  LocationDTO.builder()
                    .Hour1(entities.get(0).getHour1())
                    .X(entities.get(0).getX())
                    .Y(entities.get(0).getY())
                    .Step1(entities.get(0).getStep1())
                    .Step2(entities.get(0).getStep2())
                    .Step3(entities.get(0).getStep3())
                    .Hour2(entities.get(0).getHour2())
                    .Minute1(entities.get(0).getMinute1())
                    .Minute2(entities.get(0).getMinute2())
                    .Second1(entities.get(0).getSecond1())
                    .Second2(entities.get(0).getSecond2())
                    .build();
        }



    }


}
