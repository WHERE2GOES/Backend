package backend.greatjourney.domain.certification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CertificationRequest {
    private Long userId;
    private double latitude;
    private double longitude;
    private String hash;
}