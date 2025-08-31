package backend.greatjourney.domain.terms.service;

import backend.greatjourney.domain.terms.domain.TermsType;
import backend.greatjourney.domain.terms.dto.TermsResponseDto;
import backend.greatjourney.domain.terms.repository.TermsRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TermsService {

    private final TermsRepository termsRepository;

    public TermsResponseDto getLatestTerms(TermsType type) {
        return termsRepository.findFirstByTypeOrderByEffectiveDateDescVersionDesc(type)
                .map(TermsResponseDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.TERMS_NOT_FOUND));
    }
}