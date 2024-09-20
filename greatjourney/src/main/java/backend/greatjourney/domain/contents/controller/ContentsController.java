package backend.greatjourney.domain.contents.controller;

import backend.greatjourney.domain.contents.dto.ApiResponse;
import backend.greatjourney.domain.contents.dto.LocationBasedListResponse;
import backend.greatjourney.domain.contents.service.ContentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/map")
public class ContentsController {

    private final ContentsService contentsService;

    @Value("${tour.api_key}")
    private String serviceKey;

    @GetMapping("/location-based")
    public ResponseEntity<ApiResponse> getLocationBasedData(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "12") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "38") int contentTypeId) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/locationBasedList1")
                .queryParam("ServiceKey", serviceKey)  // 실제 서비스 키로 변경 필요
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", radius)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("listYN", "Y")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));    }

    @GetMapping("/area-based")
    public ResponseEntity<ApiResponse> getAreaBasedData(
            @RequestParam int areaCode,
            @RequestParam(required = false) Integer sigunguCode,
            @RequestParam(defaultValue = "12") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "12") int contentTypeId) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/areaBasedList1")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("areaCode", areaCode)
                .queryParam("sigunguCode", sigunguCode)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("listYN", "Y")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));    }

    @GetMapping("/category-codes")
    public ResponseEntity<ApiResponse> getCategoryCodes(
            @RequestParam int contentTypeId,
            @RequestParam(required = false) String cat1,
            @RequestParam(required = false) String cat2,
            @RequestParam(required = false) String cat3) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/categoryCode1")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("cat1", cat1)
                .queryParam("cat2", cat2)
                .queryParam("cat3", cat3)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));    }

    @GetMapping("/keyword-search")
    public ResponseEntity<ApiResponse> searchByKeyword(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "12") int contentTypeId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/searchKeyword1")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("keyword", keyword)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));
    }

    @GetMapping("/festivals")
    public LocationBasedListResponse getFestivals(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "2000") int radius,  // 기본값을 2000으로 변경
            @RequestParam(defaultValue = "12") int numOfRows, // 기본값을 12로 변경
            @RequestParam(defaultValue = "1") int pageNo) {

        // URI 빌더로 필요한 부분만 추가하고, 서비스 키는 따로 추가
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/locationBasedList1")
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", radius)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("contentTypeId", "")  // contentTypeId를 비워둡니다.
                .queryParam("listYN", "Y")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("arrange", "A");
                //.queryParam("_type", "json");


        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;
        log.info("Final URL: {}", finalUrl);


        RestTemplate restTemplate = new RestTemplate();

        String responseString = restTemplate.getForObject(finalUrl, String.class);
        log.info("Response: {}", responseString);


        LocationBasedListResponse response = restTemplate.getForObject(finalUrl, LocationBasedListResponse.class);
        return response;

    }

    @GetMapping("/stays")
    public ResponseEntity<ApiResponse> getStays(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/locationBasedList1")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", radius)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("contentTypeId", 32)  // 숙박
                .queryParam("listYN", "Y")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));    }

    @GetMapping("/tourist-spots")
    public ResponseEntity<ApiResponse> getTouristSpots(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "1000") int radius,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://apis.data.go.kr/B551011/KorService1/locationBasedList1")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", radius)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("contentTypeId", 12)  // 관광지
                .queryParam("listYN", "Y")
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "YourApp");

        // 최종 URL에 서비스 키는 수동으로 추가 (자동 인코딩 방지)
        String finalUrl = uriBuilder.toUriString() + "&ServiceKey=" + serviceKey;

        log.info("serviceKey = " + serviceKey);
        log.info("Final URL: {}", finalUrl);

        // API 호출
        return ResponseEntity.ok(contentsService.callApi(finalUrl));    }
}
