package backend.greatjourney.domain.login.controller;

import backend.greatjourney.domain.login.dto.ProfileEditRequest;
import backend.greatjourney.domain.login.dto.ProfileRequest;
import backend.greatjourney.domain.login.service.AuthenticationService;
import backend.greatjourney.domain.login.service.FileUploadService;
import backend.greatjourney.domain.login.service.JwtService;
import backend.greatjourney.domain.login.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/login/profile")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public BaseResponse<Object> profile(@RequestHeader("Authorization") String accessToken) {
        log.info("start!!!");
        String token = accessToken.substring(7);

        Long loginId = jwtService.extractUserId(token);
        log.info("loginId: "+loginId);

        Map<String, Object> profileInfo = userService.getProfileInfo(loginId);
        log.info("profileInfo: "+profileInfo.size());
        for (Map.Entry<String, Object> entry : profileInfo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        return BaseResponse.<Object>builder()
                .code(2005)
                .isSuccess(true)
                .message(loginId + "안녕하세요")
                .data(profileInfo)
                .build();
    }

    @PostMapping("/edit")
    public BaseResponse<Object> profileSave(@RequestBody ProfileEditRequest profileEditRequest, @RequestHeader("Authorization") String accessToken) {
        String token = accessToken.substring(7);
        Long loginId = jwtService.extractUserId(token);
        userService.profileSave(profileEditRequest, loginId);
        return BaseResponse.<Object>builder()
                .code(4000)
                .isSuccess(true)
                .message("프로필 등록 성공")
                .build();
    }


    @PostMapping("/image/upload")
    public BaseResponse<Object> uploadFile(@RequestParam("file")MultipartFile file, @RequestHeader("Authorization") String accessToken) throws IOException {
        String token = accessToken.substring(7);
        Long loginId = jwtService.extractUserId(token);
        String url = fileUploadService.uploadFile(file);
        userService.saveUploadImg(url, loginId);
        return BaseResponse.<Object>builder()
                .code(4001)
                .message("이미지가 업로드 되었습니다")
                .data(url)
                .build();
    }

    // 회원 탈퇴
    @DeleteMapping("/deleteUser")
    public BaseResponse<Object> withdrawUser(@RequestHeader(name="Authorization") String accessToken, HttpServletRequest request) {
        try {
            String token = accessToken.substring(7);
            Long loginId = jwtService.extractUserId(token);
            // 사용자 ID를 기반으로 회원 탈퇴 처리
            authenticationService.deleteUserById(loginId);
            return BaseResponse.<Object>builder()
                    .code(2000)
                    .isSuccess(true)
                    .message("회원 탈퇴가 완료되었습니다.")
                    .build();
        } catch (Exception e) {
            log.error("회원 탈퇴 처리 중 오류 발생: {}", e.getMessage());
            return BaseResponse.<Object>builder()
                    .code(5000)
                    .isSuccess(false)
                    .message("회원 탈퇴 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }
}
