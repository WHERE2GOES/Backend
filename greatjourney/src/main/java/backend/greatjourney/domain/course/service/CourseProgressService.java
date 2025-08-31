package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.dto.CourseEndReq;
import backend.greatjourney.domain.course.dto.CourseStartReq;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseProgressService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public void startCourse(CustomOAuth2User customOAuth2User, CourseStartReq req) {
        Integer courseId = req.courseId();
        // 1. 코스가 실제로 존재하는지 확인
        if (!courseRepository.existsById(courseId)) {
            throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
        }

        // 2. 사용자 정보를 DB에서 조회
        Long userId = Long.parseLong(customOAuth2User.getUserId());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 3. 이미 진행 중인 다른 코스가 있는지 확인
        if (user.getCurrentCourseId() != null) {
            throw new CustomException(ErrorCode.ALREADY_IN_PROGRESS_COURSE);
        }

        // 4. 사용자의 현재 진행 코스 ID를 업데이트
        user.setCurrentCourseId(courseId);
    }


    public void endCourse(CustomOAuth2User customOAuth2User, CourseEndReq req) {
        Long userId = Long.parseLong(customOAuth2User.getUserId());
        // 1. 사용자 정보를 DB에서 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 예시

        Integer currentCourseId = user.getCurrentCourseId();

        // 2. 진행 중인 코스가 없는 경우 예외 처리
        if (currentCourseId == null) {
            throw new CustomException(ErrorCode.USER_NOT_ON_COURSE);
        }

        Integer courseId = req.courseId();
        // 3. 종료하려는 코스가 현재 진행 중인 코스가 맞는지 확인
        if (!currentCourseId.equals(courseId)) {
            throw new CustomException(ErrorCode.COURSE_NOT_MATCH);
        }

        // 4. 사용자의 현재 진행 코스 ID를 null로 변경하여 종료 처리
        user.setCurrentCourseId(null);
    }
}