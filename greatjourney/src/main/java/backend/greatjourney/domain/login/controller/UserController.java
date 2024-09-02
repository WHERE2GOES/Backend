package backend.greatjourney.domain.login.controller;

import com.umc.Palette.domain.music.domain.Music;
import com.umc.Palette.domain.music.dto.request.MusicRequest;
import com.umc.Palette.domain.music.service.MusicService;
import com.umc.Palette.domain.user.dto.ProfileRequest;
import com.umc.Palette.domain.user.service.FileUploadService;
import com.umc.Palette.domain.user.service.UserService;
import com.umc.Palette.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MusicService musicService;
    private final FileUploadService fileUploadService;

    @GetMapping("/{loginId}")
    public BaseResponse<Object> hi(@PathVariable(name = "loginId") String loginId) {
        Map<String, Object> profileInfo = userService.getProfileInfo(loginId);
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

    @PostMapping("/profile/{loginId}")
    public BaseResponse<Object> profileSave(@RequestBody ProfileRequest profileRequest, @PathVariable("loginId")String loginId) {
        userService.profileSave(profileRequest, loginId);
        return BaseResponse.<Object>builder()
                .code(4000)
                .isSuccess(true)
                .message("프로필 등록 성공")
                .build();
    }

    @PostMapping("/profile/{loginId}/music")
    public BaseResponse<Object> profileSave(@RequestBody MusicRequest musicRequest, @PathVariable("loginId")String loginId) {
        Music music = musicService.findMusic(musicRequest);
        userService.profileMusicSave(music, loginId);
        return BaseResponse.<Object>builder()
                .code(4010)
                .isSuccess(true)
                .message("대표음악 등록 성공")
                .build();
    }



    @PostMapping("/profile/{loginId}/image/upload")
    public BaseResponse<Object> uploadFile(@RequestParam("file")MultipartFile file, @PathVariable("loginId")String loginId) throws IOException {
        String url = fileUploadService.uploadFile(file);
        userService.saveUploadImg(url, loginId);
        return BaseResponse.<Object>builder()
                .code(4001)
                .message("이미지가 업로드 되었습니다")
                .data(url)
                .build();
    }

    @GetMapping("/{loginId}/visit/{visitedUserId}")
    public BaseResponse<Object> visitToOther(@PathVariable("loginId")String loginId, @PathVariable("visitedUserId")String visitedUserId){
        Map<String, Object> visitedUserProfileInfo = userService.visit(visitedUserId);
        for (Map.Entry<String, Object> entry : visitedUserProfileInfo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        return BaseResponse.<Object>builder()
                .code(4001)
                .message("상대방의 프로필을 조회했습니다")
                .data(visitedUserProfileInfo)
                .build();
    }
    
}
