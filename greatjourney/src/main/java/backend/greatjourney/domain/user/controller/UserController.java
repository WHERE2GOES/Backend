package backend.greatjourney.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.token.dto.TokenResponse;
import backend.greatjourney.domain.user.dto.request.ChangeUserRequest;
import backend.greatjourney.domain.user.dto.request.LoginRequest;
import backend.greatjourney.domain.user.dto.request.SignUpRequest;
import backend.greatjourney.domain.user.service.GoogleService;
import backend.greatjourney.domain.user.service.KakaoService;
import backend.greatjourney.domain.user.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "로그인 API", description = "로그인/로그아웃 API에 대한 설명입니다.")
public class UserController {

	private final UserService userService;
	private final KakaoService kakaoService;
	private final GoogleService googleService;

	//회원가입
	@Operation(summary = "회원가입 API")
	@PostMapping("/signup")
	public BaseResponse<TokenResponse> signUp(@RequestBody SignUpRequest request){
		return userService.signupUser(request);
	}

	//회원탈퇴
	@Operation(summary = "회원탈퇴 API")
	@DeleteMapping("/signout")
	public BaseResponse<Void> singOut(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
		return userService.signOutUser(customOAuth2User);
	}

	//카카오로그인
	@Operation(summary = "카카오로그인 API")
	@PostMapping("/kakao")
	public BaseResponse<TokenResponse> loginKakao(@RequestBody LoginRequest request){
		return BaseResponse.<TokenResponse>builder()
			.isSuccess(true)
			.code(201)
			.message("카카오 로그인이 완료되었습니다.")
			.data(kakaoService.loginWithKakao(request.accessToken()))
			.build();
	}

	//구글로그인
	@Operation(summary = "구글로그인 API")
	@PostMapping("/google")
	public BaseResponse<TokenResponse> loginGoogle(@RequestBody LoginRequest request){
		return BaseResponse.<TokenResponse>builder()
			.isSuccess(true)
			.code(201)
			.message("구글 로그인이 완료되었습니다.")
			.data(googleService.loginWithGoogle(request.accessToken()))
			.build();
	}

	//로그아웃
	@Operation(summary = "로그아웃 API")
	@PostMapping("/logout")
	public BaseResponse<Void> logout(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
		return userService.logOutUser(customOAuth2User);
	}


	//회원정보수정
	@Operation(summary = "회원정보 수정 API")
	@PatchMapping("/change")
	public BaseResponse<?> chageUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody
		ChangeUserRequest request){
		return userService.changeUserInfo(customOAuth2User,request);
	}



}
