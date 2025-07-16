package backend.greatjourney.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.user.dto.ChangeUserRequest;
import backend.greatjourney.domain.user.dto.SignUpRequest;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//회원가입
	@PostMapping("/signup")
	public BaseResponse<User> signUp(@RequestBody SignUpRequest request){
		return userService.signupUser(request);
	}

	//회원탈퇴
	@DeleteMapping("/signout")
	public BaseResponse<?> singOut(@RequestParam String refreshToken){
		return userService
	}

	//로그인
	@PostMapping("/kakao")
	public BaseResponse<?> loginKakao(){

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
