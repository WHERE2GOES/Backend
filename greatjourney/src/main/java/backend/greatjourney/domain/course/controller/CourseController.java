package backend.greatjourney.domain.course.controller;

import backend.greatjourney.domain.course.dto.*;
import backend.greatjourney.domain.course.service.CourseProgressService;
import backend.greatjourney.domain.course.service.CourseService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseProgressService courseProgressService;

    /**
     * GET /api/course-master?id={courseId}
     * 코스의 전체 정보(경로 + 주변 장소)를 조회
     */
    @GetMapping("/course-master")
    public ResponseEntity<BaseResponse<CourseMasterResponse>> getCourseMaster(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("id") Integer id) {

        CourseMasterResponse courseData = courseService.getCourseMaster(id);

        return ResponseEntity.ok(
                BaseResponse.<CourseMasterResponse>builder()
                        .isSuccess(true)
                        .code(HttpStatus.OK.value())
                        .message("코스 전체 정보 조회에 성공했습니다.")
                        .data(courseData)
                        .build()
        );
    }

    /**
     * GET /api/course-detail?id={courseId}
     * 코스의 경로 정보만 조회
     */
    @GetMapping("/course-detail")
    public ResponseEntity<BaseResponse<CourseRouteResponse>> getCourseDetail(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("id") Integer id) {

        CourseRouteResponse routeData = courseService.getCourseRoute(id); // 경로 조회 서비스 호출

        return ResponseEntity.ok(
                BaseResponse.<CourseRouteResponse>builder()
                        .isSuccess(true)
                        .code(HttpStatus.OK.value())
                        .message("코스 상세 정보(경로) 조회에 성공했습니다.")
                        .data(routeData)
                        .build()
        );
    }

    /**
     * GET /api/course-places?id={courseId}
     * 코스 주변 장소 정보만 조회 (신규 추가)
     */
    @GetMapping("/course-places")
    public ResponseEntity<BaseResponse<CoursePlacesResponse>> getCoursePlaces(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam("id") Integer id) {

        CoursePlacesResponse placesData = courseService.getCoursePlaces(id); // 주변 장소 조회 서비스 호출

        return ResponseEntity.ok(
                BaseResponse.<CoursePlacesResponse>builder()
                        .isSuccess(true)
                        .code(HttpStatus.OK.value())
                        .message("코스 주변 장소 조회에 성공했습니다.")
                        .data(placesData)
                        .build()
        );
    }

    /**
     * POST /api/course/start
     * @body {"courseId": 1}
     */
    @PostMapping("/course/start")
    public ResponseEntity<String> startCourse(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestBody @Valid CourseStartReq req) {

        // 로그인된 사용자의 ID와 요청받은 courseId를 서비스로 전달
        courseProgressService.startCourse(customOAuth2User,req);
        return ResponseEntity.ok("코스 진행이 시작되었습니다.");
    }

    /**
     * POST /api/course/end
     * @body {"courseId": 1}
     */
    @PostMapping("/course/end")
    public ResponseEntity<String> endCourse(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        // 로그인된 사용자의 ID와 요청받은 courseId를 서비스로 전달
        courseProgressService.endCourse(customOAuth2User);
        return ResponseEntity.ok("코스 진행이 종료되었습니다.");
    }

}