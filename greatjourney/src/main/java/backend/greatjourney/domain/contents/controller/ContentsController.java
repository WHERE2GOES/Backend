package backend.greatjourney.domain.contents.controller;

import backend.greatjourney.domain.contents.dto.ContentsResponse;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
/*
관광정보
관광지	12
문화시설	14
행사/공연/축제	15
여행코스	25
레포츠	28
숙박	32
쇼핑	38
음식점	39
*/

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/map")
public class ContentsController {

    private final ContentsService contentsService;

    @Value("${tour.api_key}")
    private String serviceKey;

    @GetMapping("/festivals")
    public ResponseEntity<Collection<ContentsResponse>> getFestivals(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam(defaultValue = "2000") int radius,  // 기본값을 2000으로 변경
            @RequestParam(defaultValue = "12") int numOfRows, // 기본값을 12로 변경
            @RequestParam(defaultValue = "1") int pageNo) throws IOException {

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
                .queryParam("arrange", "A")
                .queryParam("_type", "json");

        return ResponseEntity.ok(contentsService.callApi(uriBuilder));
    }

}
