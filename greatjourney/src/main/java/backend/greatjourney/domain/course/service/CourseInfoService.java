package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.dto.CertificationCenterDto;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // 정보 조회이므로 readOnly = true 설정
public class CourseInfoService {

    private final PlaceRepository placeRepository;
    private final CourseRepository courseRepository;

    /**
     * 특정 코스에 포함된 모든 인증센터 목록을 조회합니다.
     * @param courseId 조회할 코스의 ID
     * @return 인증센터 DTO 리스트
     */
    public List<CertificationCenterDto> getCertificationCentersForCourse(Integer courseId) {
        // 1. 코스가 실제로 존재하는지 확인 (안정성)
        if (!courseRepository.existsById(courseId)) {
            throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
        }

        // 2. PlaceRepository를 사용해 해당 코스의 '인증센터'들을 모두 조회
        List<Place> centers = placeRepository.findByCategoryAndCourseId("인증센터", courseId);

        // 3. 조회된 Place 엔티티 리스트를 CertificationCenterDto 리스트로 변환하여 반환
        return centers.stream()
                .map(CertificationCenterDto::from)
                .toList();
    }
}