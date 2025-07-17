package backend.greatjourney.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.user.dto.request.ChangeUserRequest;
import backend.greatjourney.domain.user.dto.request.KakaoLoginRequest;
import backend.greatjourney.domain.user.dto.request.SignUpRequest;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.service.KakaoService;
import backend.greatjourney.domain.user.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final KakaoService kakaoService;

	//회원가입
	@PostMapping("/signup")
	public BaseResponse<User> signUp(@RequestBody SignUpRequest request){
		return userService.signupUser(request);
	}

	//회원탈퇴
	@DeleteMapping("/signout")
	public BaseResponse<Void> singOut(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
		return userService.signOutUser(customOAuth2User);
	}

	//카카오로그인
	@PostMapping("/kakao")
	public BaseResponse<?> loginKakao(@RequestBody KakaoLoginRequest request){
		return BaseResponse.builder()
			.isSuccess(true)
			.code(200)
			.message("로그인이 완료되었습니다.")
			.data(kakaoService.loginWithKakao(request.accessToken()))
			.build();
	}

	//로그아웃
	@PostMapping("/logout")
	public BaseResponse<Void> logout(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
		return userService.logOutUser(customOAuth2User);
	}


	//회원정보수정
	@PatchMapping("/change")
	public BaseResponse<?> chageUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody
		ChangeUserRequest request){
		return userService.changeUserInfo(customOAuth2User,request);
	}



}
