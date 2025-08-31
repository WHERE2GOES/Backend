package backend.greatjourney.domain.terms.dto;

import backend.greatjourney.domain.terms.domain.Terms;
import backend.greatjourney.domain.terms.domain.TermsType;

public record TermsResponseDto(
        TermsType type,
        String version,
        String content,
        boolean isRequired,
        String effectiveDate
) {
    public static TermsResponseDto from(Terms terms) {
        return new TermsResponseDto(
                terms.getType(),
                terms.getVersion(),
                terms.getContent(),
                terms.isRequired(),
                terms.getEffectiveDate()
        );
    }
}
