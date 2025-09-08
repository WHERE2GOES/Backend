package backend.greatjourney.domain.survey.dto;

public record CourseRecDto(
        String name,        // 1. 코스 이름
        String description, // 2. 코스 한 줄 설명
        String category     // 3. 코스가 속한 카테고리 (e.g., "난이도 상 / 자연 / 산")
) {}