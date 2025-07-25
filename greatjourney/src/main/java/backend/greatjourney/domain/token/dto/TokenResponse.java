package backend.greatjourney.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
	private String accessToken;
	private String refreshToken;
}
