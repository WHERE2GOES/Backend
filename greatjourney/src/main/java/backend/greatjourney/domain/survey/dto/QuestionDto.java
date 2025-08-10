package backend.greatjourney.domain.survey.dto;

public record QuestionDto(
        int id,
        String axis,
        String leftLabel,
        String rightLabel,
        int order
) {}
