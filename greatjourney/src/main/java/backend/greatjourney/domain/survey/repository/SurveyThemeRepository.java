package backend.greatjourney.domain.survey.repository;

import backend.greatjourney.domain.survey.domain.SurveyTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyThemeRepository extends JpaRepository<SurveyTheme, Long> {

    /**
     * 난이도, 지형, 풍경 세 가지 조건에 모두 일치하는 테마를 조회합니다.
     * @param difficulty 난이도 ("상" 또는 "하")
     * @param geography 지형 ("산" 또는 "강")
     * @param scenery 풍경 ("자연" 또는 "도시")
     * @return Optional<SurveyTheme>
     */
    Optional<SurveyTheme> findByDifficultyAndGeographyAndScenery(String difficulty, String geography, String scenery);
}
