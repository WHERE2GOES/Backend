package backend.greatjourney.domain.course.dto;

import java.util.List;

public record CourseMasterResponse(List<LatLngDto> route, List<PlaceItemDto> places) { }
