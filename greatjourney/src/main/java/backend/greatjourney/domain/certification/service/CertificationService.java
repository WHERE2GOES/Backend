package backend.greatjourney.domain.certification.service;

import backend.greatjourney.domain.certification.domain.UserCertification;
import backend.greatjourney.domain.certification.dto.CertificationRequest;
import backend.greatjourney.domain.certification.dto.CertificationStatusDto;
import backend.greatjourney.domain.certification.dto.CourseCertificationStatusResponse;
import backend.greatjourney.domain.certification.repository.UserCertificationRepository;
import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificationService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserCertificationRepository userCertificationRepository;

    public void certify(CustomOAuth2User customOAuth2User, CertificationRequest request) {
        // 1. 요청받은 placeId로 인증센터를 조회합니다.
        Place certificationCenter = placeRepository.findById(request.placeId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

        // 2. 해당 장소가 '인증센터' 카테고리가 맞는지 확인합니다.
        if (!"인증센터".equals(certificationCenter.getCategory())) {
            throw new CustomException(ErrorCode.CERTIFICATION_CENTER_NOT_FOUND);
        }

        // 3. 요청받은 hash 값과 DB에 저장된 hash 값이 일치하는지 확인합니다.
        // DB에 hash가 null이거나, 요청된 hash와 다르다면 예외를 발생시킵니다.
        if (certificationCenter.getHash() == null || !certificationCenter.getHash().equals(request.hash())) {
            throw new CustomException(ErrorCode.HASH_MISMATCH);
        }

        // 4. 사용자 정보를 조회합니다.
        User user = userRepository.findByUserId(Long.parseLong(customOAuth2User.getUserId()))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 5. 인증 정보를 생성하고 저장합니다. (이미 인증했는지 중복 체크 로직을 추가할 수도 있습니다.)
        UserCertification certification = UserCertification.builder()
                .user(user)
                .certificationCenter(certificationCenter)
                .courseId(certificationCenter.getCourseId())
                .build();

        userCertificationRepository.save(certification);
    }

    /**
     * 특정 코스에 대한 사용자의 모든 인증 내역을 조회합니다.
     */
    @Transactional(readOnly = true)
    public CourseCertificationStatusResponse getUserCertificationsForCourse(CustomOAuth2User customOAuth2User, Integer courseId) {
        Long userId = Long.parseLong(customOAuth2User.getUserId());

        // 1. 해당 코스의 전체 인증센터 개수를 조회합니다.
        long totalCertificationCenters = placeRepository.countByCategoryAndCourseId("인증센터", courseId);

        // 2. 사용자가 해당 코스에서 완료한 인증 내역을 조회합니다.
        List<UserCertification> userCertifications = userCertificationRepository.findByUser_UserIdAndCourseId(userId, courseId);

        // 3. 완료 여부를 계산합니다. (전체 개수가 0일 경우도 고려)
        boolean isCompleted = (totalCertificationCenters > 0) && (userCertifications.size() >= totalCertificationCenters);

        // 4. 인증 내역을 DTO 리스트로 변환합니다.
        List<CertificationStatusDto> certificationDtos = userCertifications.stream()
                .map(CertificationStatusDto::from)
                .toList();

        // 5. 최종 응답 DTO를 생성하여 반환합니다.
        return new CourseCertificationStatusResponse(isCompleted, certificationDtos);
    }
}