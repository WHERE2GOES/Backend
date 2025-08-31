package backend.greatjourney.domain.certification.controller;

import backend.greatjourney.domain.certification.dto.CertificationRequest;
import backend.greatjourney.domain.certification.dto.CertificationResponse;
import backend.greatjourney.domain.certification.service.CertificationService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<BaseResponse<String>> certify(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody CertificationRequest req) {
        certificationService.certify(customOAuth2User, req);
        BaseResponse<String> response = BaseResponse.<String>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("인증에 성공했습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);    }
}