package backend.greatjourney.domain.terms.service;


import backend.greatjourney.domain.terms.domain.Terms;
import backend.greatjourney.domain.terms.domain.UserTermsAgreement;
import backend.greatjourney.domain.terms.dto.TermsAgreementRequest;
import backend.greatjourney.domain.terms.repository.TermsRepository;
import backend.greatjourney.domain.terms.repository.UserTermsAgreementRepository;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TermsAgreementService {

    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final UserTermsAgreementRepository userTermsAgreementRepository;

    public void agreeToTerms(CustomOAuth2User customOAuth2User, TermsAgreementRequest request) {
        Long userId = Long.parseLong(customOAuth2User.getUserId());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 요청받은 ID 목록으로 모든 Terms 엔티티를 조회
        List<Terms> termsToAgree = termsRepository.findAllById(request.agreedTermsIds());

        // 요청 ID 개수와 실제 조회된 엔티티 개수가 다르면 잘못된 ID가 포함된 것
        if (termsToAgree.size() != request.agreedTermsIds().size()) {
            throw new CustomException(ErrorCode.TERMS_NOT_FOUND); // 혹은 다른 적절한 에러 코드
        }

        // 각 약관에 대해 동의 내역을 생성하고 저장
        List<UserTermsAgreement> agreements = termsToAgree.stream()
                .map(term -> UserTermsAgreement.builder()
                        .user(user)
                        .terms(term)
                        .build())
                .toList();

        userTermsAgreementRepository.saveAll(agreements);
    }
}
