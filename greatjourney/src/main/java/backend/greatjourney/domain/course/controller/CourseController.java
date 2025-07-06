package backend.greatjourney.domain.course.controller;

import backend.greatjourney.domain.course.dto.CourseDetailResponse;
import backend.greatjourney.domain.course.dto.PlaceDetailResponse;
import backend.greatjourney.domain.course.service.CourseService;
import backend.greatjourney.domain.course.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final PlaceService placeService;

    /**
     * GET /api/course-detail?id={courseId}
     */
    @GetMapping("/course-detail")
    public CourseDetailResponse getCourse(@RequestParam("id") Integer id){
        return courseService.getCourseDetail(id);
    }

    /**
     * GET /api/place-detail?id={placeId}
     */
    @GetMapping("/place-detail")
    public PlaceDetailResponse getPlace(@RequestParam("id") Long id){
        return placeService.getPlaceDetail(id);
    }
}