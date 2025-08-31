package backend.greatjourney.domain.course.controller;

import backend.greatjourney.domain.course.dto.CourseDetailResponse;
import backend.greatjourney.domain.course.dto.PlaceDetailResponse;
import backend.greatjourney.domain.course.service.CourseService;
import backend.greatjourney.domain.course.service.PlaceService;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public CourseDetailResponse getCourse(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestParam("id") Integer id){
        return courseService.getCourseDetail(id);
    }

    /**
     * GET /api/place-detail?id={placeId}
     */
    @GetMapping("/place-detail")
    public PlaceDetailResponse getPlace(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestParam("id") Long id){
        return placeService.getPlaceDetail(id);
    }

    //추천 경로를 제공해주는 코드를 작성하면 됨

}