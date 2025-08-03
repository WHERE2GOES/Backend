package backend.greatjourney.domain.survey.dto;

public record AnswerDto(
        int questionId,
        String choice   // "LEFT" / "RIGHT"
) {}