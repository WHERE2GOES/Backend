package backend.greatjourney.domain.survey.controller;

import backend.greatjourney.domain.survey.dto.AnswerReq;
import backend.greatjourney.domain.survey.dto.AnswerRes;
import backend.greatjourney.domain.survey.dto.QuestionDto;
import backend.greatjourney.domain.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService service;

    // ① 질문 리스트
    @GetMapping("/questions")
    public List<QuestionDto> questions() {
        return service.getQuestions();
    }

    // ② 사용자 응답 저장
    @PostMapping("/answers")
    public AnswerRes save(@RequestBody @Valid AnswerReq req) {
        service.saveAnswers(req);
        return new AnswerRes(true);
    }
}
