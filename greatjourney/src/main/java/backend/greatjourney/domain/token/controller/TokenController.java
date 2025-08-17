package backend.greatjourney.domain.token.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.token.dto.TokenResponse;
import backend.greatjourney.domain.token.service.JwtTokenProvider;
import backend.greatjourney.global.exception.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class TokenController {

	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/token")
	@Operation(summary = "토큰 재발급 API입니다.")
	public BaseResponse<TokenResponse> makeNewToken(@RequestBody String refreshToken){
		return BaseResponse.<TokenResponse>builder()
			.message("토큰이 재발급되었습니다.")
			.code(200)
			.isSuccess(true)
			.data(jwtTokenProvider.makeNewAccessToken(refreshToken))
			.build();
	}
}
