package backend.greatjourney.domain.terms.dto;

// 약관 ID 목록을 받는 간단한 record DTO
public record TermsAgreementRequest(
        java.util.List<Long> agreedTermsIds
) {}
