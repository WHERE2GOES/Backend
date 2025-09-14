package backend.greatjourney.domain.certification.dto;

import java.util.List;

public record CourseCertificationStatusResponse(
        boolean isCompleted, // 코스 완료 여부
        long totalCertificationsCount, // (추가) 해당 코스의 전체 인증소 개수
        long certifiedCount,           // (추가) 사용자가 인증한 개수
        List<CertificationStatusDto> certifications, // 기존 인증 내역 리스트
        List<UncertifiedPlaceDto> uncertifiedList   // (추가) 아직 인증하지 않은 목록
) {}