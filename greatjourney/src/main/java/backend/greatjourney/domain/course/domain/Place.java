package backend.greatjourney.domain.course.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Place {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;          // 이름
    private String category;      // 구분(인증센터, 화장실 …)
    private double latitude;      // 위도
    private double longitude;     // 경도


    private Integer courseId;     // 비밀번호
    private String hash;          // 인증용 해시 필드

}