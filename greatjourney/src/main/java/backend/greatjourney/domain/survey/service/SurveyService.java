package backend.greatjourney.domain.survey.service;

import backend.greatjourney.domain.course.domain.Course;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.survey.domain.SurveyAnswer;
import backend.greatjourney.domain.survey.dto.AnswerDto;
import backend.greatjourney.domain.survey.dto.AnswerReq;
import backend.greatjourney.domain.survey.dto.CourseRecDto;
import backend.greatjourney.domain.survey.dto.QuestionDto;
import backend.greatjourney.domain.survey.repository.CourseCriteriaRepository;
import backend.greatjourney.domain.survey.repository.SurveyAnswerRepository;
import backend.greatjourney.domain.survey.repository.SurveyQuestionRepository;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionRepository qRepo;
    private final SurveyAnswerRepository aRepo;
    private final CourseRepository cRepo;
    private final CourseCriteriaRepository ccRepo;

    public List<QuestionDto> getQuestions() {
        return qRepo.findAllByOrderByOrderIndexAsc().stream()
                .map(q -> new QuestionDto(
                        q.getId(), q.getAxis(),
                        q.getLeftLabel(), q.getRightLabel(),
                        q.getOrderIndex()))
                .toList();
    }

    @Transactional
    public void saveAnswers(CustomOAuth2User customOAuth2User,AnswerReq req) {
        for (AnswerDto dto : req.answers()) {
            SurveyAnswer ans = aRepo.findByUserIdAndQuestionId(Long.parseLong(customOAuth2User.getUserId()), dto.questionId())
                    .orElseGet(SurveyAnswer::new);
            ans.setUserId(Long.parseLong(customOAuth2User.getUserId()));
            ans.setQuestionId(dto.questionId());
            ans.setChoice(dto.choice());
            aRepo.save(ans);
        }
    }


    // ✨ 추천 로직 최종 수정
    public List<CourseRecDto> getRecommendedCourses(CustomOAuth2User customOAuth2User) {
        long userId = Long.parseLong(customOAuth2User.getUserId());
        List<SurveyAnswer> answers = aRepo.findByUserId(userId);

        if (answers.size() < 3) {
            return List.of();
        }

        // 1. 사용자의 설문 응답을 텍스트로 변환
        Map<Integer, String> userChoices = answers.stream()
                .collect(Collectors.toMap(
                        SurveyAnswer::getQuestionId,
                        answer -> mapChoiceToText(answer.getQuestionId(), answer.getChoice())
                ));

        String preferredDifficulty = userChoices.get(1L);
        String preferredScenery = userChoices.get(2L);
        String preferredGeography = userChoices.get(3L);

        // ✨ 2. CourseCriteriaRepository(ccRepo)를 사용하도록 변경
        List<Integer> matchingCourseIds = ccRepo.findCourseIdsByCriteria(
                preferredDifficulty, preferredScenery, preferredGeography
        );

        if (matchingCourseIds.isEmpty()) {
            return List.of();
        }

        // 3. ID 목록을 사용하여 DB에서 실제 Course 정보(ID, 이름)를 조회
        List<Course> recommendedCourses = cRepo.findAllByIdIn(matchingCourseIds);

        // 4. DTO로 변환하여 반환
        return recommendedCourses.stream()
                .map(course -> new CourseRecDto(
                        course.getName(),
                        // 여기에 들어갈 설명은 DB에 없으므로, 직접 지정하거나 DTO에서 제외할 수 있습니다.
                        "당신의 취향에 맞는 코스입니다.",
                        String.format("난이도 %s / %s / %s", preferredDifficulty, preferredScenery, preferredGeography)
                ))
                .collect(Collectors.toList());
    }

    private String mapChoiceToText(Integer questionId, String choice) {
        int choiceValue = Integer.parseInt(choice);
        return switch (questionId.intValue()) {
            case 1 -> (choiceValue == 0) ? "상" : "하";
            case 2 -> (choiceValue == 0) ? "자연" : "도시";
            case 3 -> (choiceValue == 0) ? "산" : "강";
            default -> throw new IllegalArgumentException("Invalid question ID: " + questionId);
        };
    }
}

