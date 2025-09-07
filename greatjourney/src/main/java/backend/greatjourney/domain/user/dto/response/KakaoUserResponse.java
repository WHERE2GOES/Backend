package backend.greatjourney.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class KakaoUserResponse {
	private Long id;
	private KakaoAccount kakao_account;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class KakaoAccount {
		private String email;
	}

}
