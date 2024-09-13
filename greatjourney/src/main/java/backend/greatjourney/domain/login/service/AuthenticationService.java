package backend.greatjourney.domain.login.service;

import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.*;

import java.util.Optional;


public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    boolean kakaoIdExists(String kakaoEmail);

    Optional<User> kakaoSignup(String email, String nickname, String profileImgUrl);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse kakaoSignin(String email);


    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    Boolean checkLoginIdDuplicate(String loginEmail);

    boolean passwordCheck(String pw1, String pw2);

}
