package backend.greatjourney.domain.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendationResponseDto {
    private List<Integer> recommendedCourseIds;
}
