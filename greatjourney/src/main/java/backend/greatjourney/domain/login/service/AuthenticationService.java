package backend.greatjourney.domain.login.service;

import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.*;


public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    User kakaoSignup(String email, String nickname);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse kakaoSignin(KakaoSigninRequest kakaoSigninRequest);


    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    Boolean checkLoginIdDuplicate(String loginEmail);

    boolean passwordCheck(String pw1, String pw2);

}
