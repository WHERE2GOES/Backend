package backend.greatjourney.domain.course.controller;

import backend.greatjourney.domain.course.dto.CourseDetailResponse;
import backend.greatjourney.domain.course.dto.CourseProgressRequest;
import backend.greatjourney.domain.course.dto.PlaceDetailResponse;
import backend.greatjourney.domain.course.service.CourseProgressService;
import backend.greatjourney.domain.course.service.CourseService;
import backend.greatjourney.domain.course.service.PlaceService;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final PlaceService placeService;
    private final CourseProgressService courseProgressService;

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

    /**
     * POST /api/course/start
     * @body {"courseId": 1}
     */
    @PostMapping("/course/start")
    public ResponseEntity<String> startCourse(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("id") Long id,
            @RequestBody CourseProgressRequest request) {

        // 로그인된 사용자의 ID와 요청받은 courseId를 서비스로 전달
        courseProgressService.startCourse(id, request.getCourseId());
        return ResponseEntity.ok("코스 진행이 시작되었습니다.");
    }

    /**
     * POST /api/course/end
     * @body {"courseId": 1}
     */
    @PostMapping("/course/end")
    public ResponseEntity<String> endCourse(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("id") Long id,
            @RequestBody CourseProgressRequest request) {

        // 로그인된 사용자의 ID와 요청받은 courseId를 서비스로 전달
        courseProgressService.endCourse(id, request.getCourseId());
        return ResponseEntity.ok("코스 진행이 종료되었습니다.");
    }

}