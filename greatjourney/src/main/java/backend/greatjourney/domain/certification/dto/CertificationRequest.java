package backend.greatjourney.domain.certification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public record CertificationRequest(
        Long placeId,
        String hash
) {}