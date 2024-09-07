package backend.greatjourney.domain.login.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String token;
}
