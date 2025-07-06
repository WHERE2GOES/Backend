package backend.greatjourney.domain.course.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Course {
    @Id
    private Integer id;            // ROAD_SN
    private String name;           // 노선명
}