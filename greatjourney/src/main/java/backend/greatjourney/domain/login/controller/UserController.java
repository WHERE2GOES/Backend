package backend.greatjourney.domain.login.controller;

import backend.greatjourney.domain.login.dto.ProfileEditRequest;
import backend.greatjourney.domain.login.dto.ProfileRequest;
import backend.greatjourney.domain.login.service.FileUploadService;
import backend.greatjourney.domain.login.service.JwtService;
import backend.greatjourney.domain.login.service.UserService;
import backend.greatjourney.global.exception.BaseResponse;
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

}
