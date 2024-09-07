package backend.greatjourney.domain.login.service.impl;

import backend.greatjourney.domain.login.Role;
import backend.greatjourney.domain.login.converter.SignupConverter;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.*;
import backend.greatjourney.domain.login.repository.UserRepository;
import backend.greatjourney.domain.login.service.AuthenticationService;
import backend.greatjourney.domain.login.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public User signup(SignUpRequest signUpRequest) {

        //ser user = SignupConverter.toUser(signUpRequest, Role.ROLE_USER);


        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // User 객체 생성
        User user = User.builder()
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(encodedPassword) // 암호화된 비밀번호 설정
                //.password(signUpRequest.getPassword())
                .residence(signUpRequest.getResidence())
                .gender(signUpRequest.isGender())
                .phone(signUpRequest.getPhone())
                .sns(signUpRequest.isSns())
                .role(Role.ROLE_USER)
                .build();

        // 사용자 저장
        return userRepository.save(user);

    }

    @Override
    public User kakaoSignup(String email, String nickname) { // 카카오 회원가입

//        if (userRepository.existsByEmail(email)) {
//            return userRepository.findByEmail(email);
//        }
//        User user = new User();
//
//        user.setEmail(email);
//        user.setName(nickname);
//        user.setRole(Role.ROLE_USER);
//        user.setPassword(passwordEncoder.encode(generateRandomPassword()));
//        user.setNickname(nickname);
//        user.setIntroduction("");
//        user.setLoginId(email);
//
//        return userRepository.save(user);
        return null;

    }


    public JwtAuthenticationResponse signin(SignInRequest signinRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getEmail(), signinRequest.getPassword()));

        var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid login_Id or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;

    }

    public JwtAuthenticationResponse kakaoSignin (KakaoSigninRequest kakaoSigninRequest) {
        var user = userRepository.findByEmail(kakaoSigninRequest.getLoginId()).orElseThrow(() -> new IllegalArgumentException("Invalid login_Id"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;

    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userLoginId = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userLoginId).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

    public Boolean checkLoginIdDuplicate(String loginEmail) {
        return userRepository.existsByEmail(loginEmail);
    }


    public boolean passwordCheck(String firstpassword, String secondpassword) {
        return firstpassword.equals(secondpassword);
    }
}