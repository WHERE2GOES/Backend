package backend.greatjourney.domain.certification.controller;

import backend.greatjourney.domain.certification.dto.CertificationRequest;
import backend.greatjourney.domain.certification.dto.CertificationResponse;
import backend.greatjourney.domain.certification.dto.CertificationStatusDto;
import backend.greatjourney.domain.certification.service.CertificationService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/certifications?courseId={courseId}
     * 특정 코스에 대한 사용자의 인증 내역 조회
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<CertificationStatusDto>>> getCertificationsByCourse(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("courseId") Integer courseId) {

        List<CertificationStatusDto> certificationData = certificationService.getUserCertificationsForCourse(customOAuth2User, courseId);

        return ResponseEntity.ok(
                BaseResponse.<List<CertificationStatusDto>>builder()
                        .isSuccess(true)
                        .code(200)
                        .message("코스별 인증 내역 조회에 성공했습니다.")
                        .data(certificationData)
                        .build()
        );
    }
}