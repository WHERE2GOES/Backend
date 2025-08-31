package backend.greatjourney.domain.certification.dto;


import backend.greatjourney.domain.certification.domain.UserCertification;

import java.time.LocalDateTime;

public record CertificationStatusDto(
        Long placeId,
        String placeName,
        LocalDateTime certifiedAt
) {
    public static CertificationStatusDto from(UserCertification certification) {
        return new CertificationStatusDto(
                certification.getCertificationCenter().getId(),
                certification.getCertificationCenter().getName(),
                certification.getCertifiedAt()
        );
    }
}