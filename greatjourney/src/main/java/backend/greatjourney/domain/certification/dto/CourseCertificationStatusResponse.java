package backend.greatjourney.domain.certification.dto;

import java.util.List;

public record CourseCertificationStatusResponse(
        boolean isCompleted, // 코스 완료 여부
        List<CertificationStatusDto> certifications // 기존 인증 내역 리스트
) {}