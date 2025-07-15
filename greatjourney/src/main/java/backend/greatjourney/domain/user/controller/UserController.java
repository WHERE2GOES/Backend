package backend.greatjourney.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.user.dto.SignUpRequest;
import backend.greatjourney.domain.user.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//회원가입
	@PostMapping("/signup")
	public BaseResponse<?> signUp(@RequestBody SignUpRequest request){
		return userService.signupUser(request);
	}

	//회원탈퇴

	//로그인

	//로그아웃




}
