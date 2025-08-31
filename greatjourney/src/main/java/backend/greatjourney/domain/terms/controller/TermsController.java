package backend.greatjourney.domain.terms.controller;

import backend.greatjourney.domain.terms.domain.TermsType;
import backend.greatjourney.domain.terms.dto.TermsResponseDto;
import backend.greatjourney.domain.terms.service.TermsService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    @GetMapping
    public ResponseEntity<BaseResponse<TermsResponseDto>> getTerms(
            @RequestParam("type") TermsType type) {

        TermsResponseDto termsData = termsService.getLatestTerms(type);

        return ResponseEntity.ok(
                BaseResponse.<TermsResponseDto>builder()
                        .isSuccess(true)
                        .code(200)
                        .message(type.name() + " 약관 조회에 성공했습니다.")
                        .data(termsData)
                        .build()
        );
    }
}