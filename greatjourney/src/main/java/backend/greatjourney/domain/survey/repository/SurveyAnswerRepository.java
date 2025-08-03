package backend.greatjourney.domain.survey.repository;

import backend.greatjourney.domain.survey.domain.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer,Long> {
    List<SurveyAnswer> findByUserId(Long userId);
    Optional<SurveyAnswer> findByUserIdAndQuestionId(Long userId, Integer qid);
}
