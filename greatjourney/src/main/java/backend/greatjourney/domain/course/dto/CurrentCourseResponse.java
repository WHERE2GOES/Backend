package backend.greatjourney.domain.course.dto;

public record CurrentCourseResponse(
        boolean isRiding,
        Integer courseId,
        String courseName
) {
}
