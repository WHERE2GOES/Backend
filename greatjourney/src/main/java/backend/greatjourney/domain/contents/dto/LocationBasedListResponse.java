package backend.greatjourney.domain.contents.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LocationBasedListResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;  // JSON 응답에 맞게 item 리스트
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Item {
        private String addr1;
        private String addr2;
//        private String areacode;
//        private String booktour;
//        private String cat1;
//        private String cat2;
//        private String cat3;
//        private String contentid;
//        private String contenttypeid;
//        private String createdtime;
//        private String cpyrhtDivCd;

        //중심 좌표로 부터의 거리(미터)//
        private double dist;
        //본 이미지//
        private String firstimage;
        //썸네일 이미지//
        private String firstimage2;
        //x좌표//
        private String mapx;
        //y좌표//
        private String mapy;
//        private String mlevel;
//        private String modifiedtime;
//        private String sigungucode;
        //전화번호//
        private String tel;
        //컨텐츠이름//
        private String title;
    }
}
