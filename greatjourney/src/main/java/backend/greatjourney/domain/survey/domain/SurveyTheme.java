package backend.greatjourney.domain.survey.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "survey_theme")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 설문 조건
    @Column(nullable = false)
    private String difficulty; // "상" 또는 "하"

    @Column(nullable = false)
    private String geography;  // "산" 또는 "강"

    @Column(nullable = false)
    private String scenery;    // "자연" 또는 "도시"

    // 매핑될 테마 정보
    @Column(name = "theme_name", nullable = false)
    private String themeName;  // "성큼성큼"

    @Column(name = "theme_code", nullable = false)
    private String themeCode;  // "44444551"
}