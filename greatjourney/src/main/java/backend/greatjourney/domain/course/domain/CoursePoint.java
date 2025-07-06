package backend.greatjourney.domain.course.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_point")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CoursePoint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private int seq;          // 순서
    private double latitude;  // 위도
    private double longitude; // 경도
}