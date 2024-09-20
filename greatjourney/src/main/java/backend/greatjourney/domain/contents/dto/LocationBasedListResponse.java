package backend.greatjourney.domain.contents.dto;

import lombok.Data;

import java.util.List;

@Data
public class LocationBasedListResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;

        // Getter, Setter
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;

        // Getter, Setter
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        // Getter, Setter
    }

    @Data
    public static class Items {
        private List<Item> item;

        // Getter, Setter
    }

    @Data
    public static class Item {
        private String addr1;
        private String addr2;
        private String areacode;
        private String booktour;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentid;
        private String contenttypeid;
        private String createdtime;
        private double dist;
        private String firstimage;
        private String firstimage2;
        private String mapx;
        private String mapy;
        private String mlevel;
        private String modifiedtime;
        private String sigungucode;
        private String tel;
        private String title;

        // Getter, Setter
    }

    // Getter, Setter for Response
}
