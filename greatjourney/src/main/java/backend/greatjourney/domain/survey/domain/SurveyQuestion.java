package backend.greatjourney.domain.survey.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "survey_question")
@Getter @Setter
public class SurveyQuestion {

    @Id
    private Integer id;

    private String axis;

    @Column(name = "left_label")
    private String leftLabel;

    @Column(name = "right_label")
    private String rightLabel;

    @Column(name = "q_order")
    private Integer orderIndex;   // ← 필드명 변경
}

