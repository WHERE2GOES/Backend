package backend.greatjourney.domain.survey.service;

import backend.greatjourney.domain.course.domain.Course;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.survey.domain.CourseCriteria;
import backend.greatjourney.domain.survey.domain.SurveyAnswer;
import backend.greatjourney.domain.survey.dto.*;
import backend.greatjourney.domain.survey.repository.CourseCriteriaRepository;
import backend.greatjourney.domain.survey.repository.SurveyAnswerRepository;
import backend.greatjourney.domain.survey.repository.SurveyQuestionRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
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

    public RecommendationResponseDto recommendCourses(CustomOAuth2User customOAuth2User) {
        Long userId = Long.parseLong(customOAuth2User.getUserId());

        // 1. 사용자의 설문 답변 전체 조회
        List<SurveyAnswer> userAnswers = aRepo.findByUserId(userId);

        // 설문을 3개 미만으로 응답한 경우 예외 처리
        if (userAnswers.size() < 3) {
            throw new CustomException(ErrorCode.SURVEY_NOT_COMPLETED); // ErrorCode에 추가 필요
        }

        // 2. 답변을 선호 기준으로 변환
        Map<String, String> userPreferences = parseUserPreferences(userAnswers);

        // 3. 모든 코스 정보 조회
        List<CourseCriteria> allCourses = ccRepo.findAll();

        // 4. 각 코스별 점수 계산 및 상위 3개 추천
        List<Integer> recommendedIds = allCourses.stream()
                // 각 코스를 (courseId, score) 쌍으로 변환
                .map(course -> {
                    int score = calculateMatchScore(course, userPreferences);
                    return new CourseScore(course.getCourseId(), score);
                })
                // 점수가 높은 순으로 정렬 (점수가 같으면 id 오름차순)
                .sorted(Comparator.comparingInt(CourseScore::getScore).reversed()
                        .thenComparing(CourseScore::getCourseId))
                // 상위 3개만 선택
                .limit(3)
                // courseId만 추출하여 리스트로 변환
                .map(CourseScore::getCourseId)
                .collect(Collectors.toList());

        return new RecommendationResponseDto(recommendedIds);
    }

    /**
     * DB에서 조회한 답변 리스트를 해석하여 사용자의 선호도 Map으로 변환하는 메서드
     */
    private Map<String, String> parseUserPreferences(List<SurveyAnswer> answers) {
        Map<String, String> preferences = new HashMap<>();
        for (SurveyAnswer answer : answers) {
            switch (answer.getQuestionId()) {
                case 1: // 난이도 (상/하)
                    preferences.put("difficulty", "0".equals(answer.getChoice()) ? "상" : "하");
                    break;
                case 2: // 지형 (산/강)
                    preferences.put("geography", "0".equals(answer.getChoice()) ? "산" : "강");
                    break;
                case 3: // 풍경 (자연/도시)
                    preferences.put("scenery", "0".equals(answer.getChoice()) ? "자연" : "도시");
                    break;
            }
        }
        return preferences;
    }

    /**
     * 특정 코스가 사용자의 선호도와 얼마나 일치하는지 점수를 계산하는 메서드
     */
    private int calculateMatchScore(CourseCriteria course, Map<String, String> preferences) {
        int score = 0;
        if (course.getDifficulty().equals(preferences.get("difficulty"))) {
            score++;
        }
        if (course.getGeography().equals(preferences.get("geography"))) {
            score++;
        }
        if (course.getScenery().equals(preferences.get("scenery"))) {
            score++;
        }
        return score;
    }

    // 점수 계산 및 정렬을 위한 내부 헬퍼 클래스
    @Getter
    private static class CourseScore {
        private final Integer courseId;
        private final int score;

        CourseScore(Integer courseId, int score) {
            this.courseId = courseId;
            this.score = score;
        }
    }
}

