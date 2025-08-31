package backend.greatjourney.domain.terms.controller;

import backend.greatjourney.domain.terms.dto.TermsAgreementRequest;
import backend.greatjourney.domain.terms.service.TermsAgreementService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terms-agreement")
@RequiredArgsConstructor
public class TermsAgreementController {

    private final TermsAgreementService termsAgreementService;

    @PostMapping
    public ResponseEntity<BaseResponse<String>> agreeToTerms(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody TermsAgreementRequest request) {

        termsAgreementService.agreeToTerms(customOAuth2User, request);

        return ResponseEntity.ok(
                BaseResponse.<String>builder()
                        .isSuccess(true)
                        .code(200)
                        .message("약관 동의가 성공적으로 처리되었습니다.")
                        .data(null)
                        .build()
        );
    }
}