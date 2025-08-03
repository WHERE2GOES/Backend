package backend.greatjourney.domain.survey.repository;

import backend.greatjourney.domain.survey.domain.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionRepository
        extends JpaRepository<SurveyQuestion, Integer> {

    List<SurveyQuestion> findAllByOrderByOrderIndexAsc();   // ← 메서드명 변경
}