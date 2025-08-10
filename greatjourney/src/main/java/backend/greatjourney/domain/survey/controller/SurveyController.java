package backend.greatjourney.domain.survey.controller;

import backend.greatjourney.domain.survey.dto.AnswerReq;
import backend.greatjourney.domain.survey.dto.AnswerRes;
import backend.greatjourney.domain.survey.dto.QuestionDto;
import backend.greatjourney.domain.survey.service.SurveyService;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public AnswerRes save(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody @Valid AnswerReq req) {
        service.saveAnswers(customOAuth2User,req);
        return new AnswerRes(true);
    }
}
