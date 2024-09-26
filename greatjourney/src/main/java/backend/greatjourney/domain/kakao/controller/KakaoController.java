package backend.greatjourney.domain.kakao.controller;

import backend.greatjourney.domain.kakao.service.KakaoApi;
import backend.greatjourney.domain.login.dto.JwtAuthenticationResponse;
import backend.greatjourney.domain.login.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/kakao/auth")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoApi kakaoApi;
    private final AuthenticationService authenticationService;
    @Value("${kakao.native_app_key}")
    String nativeAppKey;  // NATIVE_APP_KEY 설정

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
        return "kakaologin";
    }

    @GetMapping("/logout")
    public String logoutForm(Model model){

        return "kakaologin";
    }

    @GetMapping("/login/oauth2")
    public ResponseEntity loginByKakao(@RequestParam("code") String code, HttpServletResponse response) throws IOException {

        System.out.println("인가 코드 받기 시작");
        // 1. 인가 코드 받기 (@RequestParam String code)

        // 2. 토큰 받기
        System.out.println("토큰 받기 시작");
        String accessToken = kakaoApi.getAccessToken(code);
        System.out.println("accessToken: "+ accessToken);

        // 3. 사용자 정보 받기
        System.out.println("사용자 정보 받기 시작");
        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);

        String email = (String)userInfo.get("email");
        String picture = (String)userInfo.get("picture");
        String nickname = (String)userInfo.get("nickname");


        System.out.println("email = " + email);
        System.out.println("picture = " + picture);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);


        // 3. DB에서 이메일로 사용자가 있는지 확인 (있으면 로그인, 없으면 회원가입)
        if (authenticationService.kakaoIdExists(email)) {
            // 사용자 정보가 존재하면 로그인 (JWT 발급)
            //JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.kakaoSignin(email);

        } else {
            // 사용자가 없으면 회원가입 후 로그인
            authenticationService.kakaoSignup(email, nickname, picture);
            //authenticationService.kakaoSignin(email);
        }
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.kakaoSignin(email);

        // 5. 최종적으로 클라이언트에 리디렉션 (NATIVE_APP_KEY를 사용해 kakao${NATIVE_APP_KEY}://oauth2 형태로 리디렉션)

        String redirectUrl = "kakao" + nativeAppKey + "://oauth2?accessToken=" + jwtAuthenticationResponse.getAccessToken() + "&refreshToken=" + jwtAuthenticationResponse.getRefreshToken();

        log.info(redirectUrl);
        response.sendRedirect(redirectUrl);  // 클라이언트로 리디렉션

        return ResponseEntity.ok().build();
        //return ResponseEntity.ok(jwtAuthenticationResponse);
    }

//    @GetMapping("/login/oauth2/{code}")
//    public ResponseEntity<String> loginByKakao(@RequestParam("code") String code) throws JsonProcessingException {
//        return ResponseEntity.ok("성공!!, code: " + code);
//    }
}