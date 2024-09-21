package backend.greatjourney.domain.kakao.controller;

import backend.greatjourney.domain.login.dto.JwtAuthenticationResponse;
import backend.greatjourney.domain.login.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/api/kakao/auth")
@RequiredArgsConstructor
public class KakaoController {

    private final  KakaoApi kakaoApi;
    private final AuthenticationService authenticationService;

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
//
//    @GetMapping("/login/oauth2/code/kakao")
//    public ResponseEntity<JwtAuthenticationResponse> loginByKakao(@RequestParam("code") String code) throws JsonProcessingException {
//
//        System.out.println("인가 코드 받기 시작");
//        // 1. 인가 코드 받기 (@RequestParam String code)
//
//        // 2. 토큰 받기
//        System.out.println("토큰 받기 시작");
//        String accessToken = kakaoApi.getAccessToken(code);
//        System.out.println("accessToken: "+ accessToken);
//
//        // 3. 사용자 정보 받기
//        System.out.println("사용자 정보 받기 시작");
//        Map<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);
//
//        String email = (String)userInfo.get("email");
//        String picture = (String)userInfo.get("picture");
//        String nickname = (String)userInfo.get("nickname");
//
//
//        System.out.println("email = " + email);
//        System.out.println("picture = " + picture);
//        System.out.println("nickname = " + nickname);
//        System.out.println("accessToken = " + accessToken);
//
//
//        // 3. DB에서 이메일로 사용자가 있는지 확인 (있으면 로그인, 없으면 회원가입)
//        if (authenticationService.kakaoIdExists(email)) {
//            // 사용자 정보가 존재하면 로그인 (JWT 발급)
//            return ResponseEntity.ok(authenticationService.kakaoSignin(email));
//
//        } else {
//            // 사용자가 없으면 회원가입 후 로그인
//            authenticationService.kakaoSignup(email, nickname, picture);
//            return ResponseEntity.ok(authenticationService.kakaoSignin(email));
//        }
//    }

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<String> loginByKakao(@RequestParam("code") String code) throws JsonProcessingException {
        return ResponseEntity.ok("성공!!, code: " + code);
    }
}