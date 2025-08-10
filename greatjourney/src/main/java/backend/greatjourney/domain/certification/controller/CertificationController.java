package backend.greatjourney.domain.certification.controller;

import backend.greatjourney.domain.certification.dto.CertificationRequest;
import backend.greatjourney.domain.certification.dto.CertificationResponse;
import backend.greatjourney.domain.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CertificationResponse> certify(@RequestBody CertificationRequest request) {
        certificationService.certify(request);
        return ResponseEntity.ok(new CertificationResponse("인증에 성공했습니다."));
    }
}