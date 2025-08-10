package backend.greatjourney.domain.survey.service;

import backend.greatjourney.domain.survey.domain.SurveyAnswer;
import backend.greatjourney.domain.survey.dto.AnswerDto;
import backend.greatjourney.domain.survey.dto.AnswerReq;
import backend.greatjourney.domain.survey.dto.QuestionDto;
import backend.greatjourney.domain.survey.repository.SurveyAnswerRepository;
import backend.greatjourney.domain.survey.repository.SurveyQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionRepository qRepo;
    private final SurveyAnswerRepository aRepo;

    public List<QuestionDto> getQuestions() {
        return qRepo.findAllByOrderByOrderIndexAsc().stream()
                .map(q -> new QuestionDto(
                        q.getId(), q.getAxis(),
                        q.getLeftLabel(), q.getRightLabel(),
                        q.getOrderIndex()))
                .toList();
    }

    @Transactional
    public void saveAnswers(AnswerReq req) {
        for (AnswerDto dto : req.answers()) {
            SurveyAnswer ans = aRepo.findByUserIdAndQuestionId(req.userId(), dto.questionId())
                    .orElseGet(SurveyAnswer::new);
            ans.setUserId(req.userId());
            ans.setQuestionId(dto.questionId());
            ans.setChoice(dto.choice());
            aRepo.save(ans);
        }
    }
}

