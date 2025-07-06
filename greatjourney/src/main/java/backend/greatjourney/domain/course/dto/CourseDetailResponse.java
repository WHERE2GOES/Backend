package backend.greatjourney.domain.course.dto;


import java.util.List;

public record CourseDetailResponse(List<LatLngDto> route, List<PlaceItemDto> places) { }
