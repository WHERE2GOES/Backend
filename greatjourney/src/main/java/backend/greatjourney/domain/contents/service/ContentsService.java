package backend.greatjourney.domain.contents.service;

import backend.greatjourney.domain.contents.dto.ApiResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.StringReader;

@Service
public class ContentsService {

    private final String SERVICE_KEY = "YOUR_SERVICE_KEY";  // 발급받은 서비스 키로 교체
    private final String BASE_URL = "http://apis.data.go.kr/B551011/KorService1";

    // API 호출을 위한 공통 메소드
    public ApiResponse callApi(String finalUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(finalUrl, String.class);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ApiResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (ApiResponse) unmarshaller.unmarshal(new StringReader(xmlResponse));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null; // 또는 적절한 예외 처리
        }
    }
}
