package backend.greatjourney.domain.survey.dto;

import java.util.List;

public record AnswerReq(
        List<AnswerDto> answers
) {}