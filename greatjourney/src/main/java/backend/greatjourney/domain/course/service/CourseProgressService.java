package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseProgressService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * 사용자의 코스 진행을 시작합니다.
     * @param userId 로그인된 사용자의 ID
     * @param courseId 시작할 코스의 ID
     */
    public void startCourse(Long userId, Integer courseId) {
        // 1. 코스가 실제로 존재하는지 확인
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("요청한 courseId에 해당하는 코스가 존재하지 않습니다.");
        }

        // 2. 사용자 정보를 DB에서 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 이미 진행 중인 다른 코스가 있는지 확인
        if (user.getCurrentCourseId() != null) {
            throw new IllegalStateException("이미 진행 중인 코스가 있습니다. 기존 코스를 먼저 종료해주세요.");
        }

        // 4. 사용자의 현재 진행 코스 ID를 업데이트
        user.setCurrentCourseId(courseId);
    }

    /**
     * 사용자의 코스 진행을 종료합니다.
     * @param userId 로그인된 사용자의 ID
     * @param courseId 종료할 코스의 ID
     */
    public void endCourse(Long userId, Integer courseId) {
        // 1. 사용자 정보를 DB에서 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Integer currentCourseId = user.getCurrentCourseId();

        // 2. 진행 중인 코스가 없는 경우 예외 처리
        if (currentCourseId == null) {
            throw new IllegalStateException("현재 진행 중인 코스가 없습니다.");
        }

        // 3. 종료하려는 코스가 현재 진행 중인 코스가 맞는지 확인
        if (!currentCourseId.equals(courseId)) {
            throw new IllegalArgumentException("현재 진행 중인 코스와 종료 요청 코스가 일치하지 않습니다.");
        }

        // 4. 사용자의 현재 진행 코스 ID를 null로 변경하여 종료 처리
        user.setCurrentCourseId(null);
    }
}