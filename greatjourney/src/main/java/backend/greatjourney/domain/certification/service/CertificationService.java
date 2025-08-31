package backend.greatjourney.domain.certification.service;

import backend.greatjourney.domain.certification.domain.UserCertification;
import backend.greatjourney.domain.certification.dto.CertificationRequest;
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
    private static final double CERTIFICATION_RADIUS_METER = 100; // 100미터

    public void certify(CustomOAuth2User customOAuth2User, CertificationRequest request) {
        // 1단계: 검색할 사각형 범위 계산
        double lat = request.getLatitude();
        double lon = request.getLongitude();
        // 위도/경도 1도당 대략적인 거리 (단순화된 계산)
        double latDiff = CERTIFICATION_RADIUS_METER / 111000.0;
        double lonDiff = CERTIFICATION_RADIUS_METER / (111000.0 * Math.cos(Math.toRadians(lat)));

        // BoundingBox를 이용해 1차 필터링
        List<Place> candidates = placeRepository.findInBoundingBox(
                lat - latDiff, lat + latDiff,
                lon - lonDiff, lon + lonDiff
        );

        // 2단계: 실제 거리 계산 후 2차 필터링 및 해시값 비교
        Place certificationCenter = candidates.stream()
                .filter(place -> "인증센터".equals(place.getCategory())) // 인증센터만 필터링
                .filter(place -> calculateDistance(lat, lon, place.getLatitude(), place.getLongitude()) <= CERTIFICATION_RADIUS_METER) // 반경 내 필터링
                .filter(place -> place.getHash() == null || place.getHash().equals(request.getHash())) // 해시값 비교
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CERTIFICATION_CENTER_NOT_FOUND));

        User user = userRepository.findByUserId(Long.parseLong(customOAuth2User.getUserId()))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserCertification certification = UserCertification.builder()
                .user(user)
                .certificationCenter(certificationCenter)
                .build();

        userCertificationRepository.save(certification);
    }

    /**
     * Haversine formula를 이용한 두 지점 간의 거리 계산 (미터 단위)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반경 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // 미터 단위로 변환
    }
}