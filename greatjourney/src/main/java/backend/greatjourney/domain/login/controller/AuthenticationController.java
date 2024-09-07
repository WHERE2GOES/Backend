package backend.greatjourney.domain.login.controller;

import backend.greatjourney.domain.login.dto.*;
import backend.greatjourney.domain.login.service.AuthenticationService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.exception.BaseResponseService;
import backend.greatjourney.global.exception.CustomExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/login/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final BaseResponseService baseResponseService;

    private final CustomExceptionHandler customExceptionHandler;

    // 회원가입 - (1) 아이디 중복 확인
    @GetMapping("signup/{loginEmail}")
    public BaseResponse<Object> checkLoginIdDuplicate(@PathVariable(name = "loginEmail") String loginEmail) {
        if (authenticationService.checkLoginIdDuplicate(loginEmail)) {
            return BaseResponse.<Object>builder()
                    .code(2100)
                    .isSuccess(false)
                    .message("아이디가 이미 존재합니다.")
                    .build();
        } else {
            return BaseResponse.<Object>builder()
                    .code(2000)
                    .isSuccess(true)
                    .message("해당 아이디를 사용할 수 있습니다.")
                    .build();
        }
    }

    // 회원가입 - (2) 비밀번호 조건 확인
    @PostMapping("/signup/passwordCheck")
    public BaseResponse<Object> passwordCheck(@RequestBody PasswordCheckRequest passwordCheckRequest) {
        // 제약 검증
        if (!passwordCheckRequest.getFirstPassword().matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")) {
            // 제약 조건을 만족하지 않는 경우
            return BaseResponse.<Object>builder()
                    .code(2102)
                    .isSuccess(false)
                    .message("영어(대/소문), 숫자, 특수문자를 포함해주세요.")
                    .build();
        }

        if (authenticationService.passwordCheck(passwordCheckRequest.getFirstPassword(), passwordCheckRequest.getSecondPassword())) {
            return BaseResponse.<Object>builder()
                    .code(2101)
                    .isSuccess(true)
                    .message("비밀번호가 확인되었습니다.")
                    .build();
        } else {
            return BaseResponse.<Object>builder()
                    .code(2001)
                    .isSuccess(false)
                    .message("비밀번호가 일치하지 않습니다.")
                    .build();
        }
    }

    /**
     *  회원가입 - (3) 이메일 인증 확인 (MailController에서 진행)
     *  회원가입 - (4) 유저가 받은 인증번호 일치 확인 (MailController에서 진행)
     */

    // 회원가입 - (5) 최종 회원가입 완료
    @PostMapping("/signup")
    public BaseResponse<Object> signup(@RequestBody SignUpRequest signUpRequest) {
        authenticationService.signup(signUpRequest);
        return BaseResponse.<Object>builder()
                .code(2004)
                .isSuccess(true)
                .message("회원가입이 완료되었습니다.")
                .build();
    }


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signinRequest) {

        return ResponseEntity.ok(authenticationService.signin(signinRequest));

    }

    @PostMapping("/kakao/signin")
    public ResponseEntity<JwtAuthenticationResponse> kakaoSignin(@RequestBody KakaoSigninRequest kakaoSigninRequest) {
        return ResponseEntity.ok(authenticationService.kakaoSignin(kakaoSigninRequest));

    }



    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));

    }

}
