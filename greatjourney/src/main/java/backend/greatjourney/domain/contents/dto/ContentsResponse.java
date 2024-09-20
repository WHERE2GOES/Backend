package backend.greatjourney.domain.contents.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentsResponse {
    private String address;

    //중심 좌표로 부터의 거리(미터)//
    private double distance;
    //본 이미지//
    private String mainImageUrl;
    //썸네일 이미지//
    private String thumbnailUrl;
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
