package backend.greatjourney.domain.survey.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_criteria")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Integer courseId;

    private String difficulty;
    private String scenery;
    private String geography;
}