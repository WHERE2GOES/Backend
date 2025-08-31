package backend.greatjourney.domain.course.controller;

import backend.greatjourney.domain.course.dto.*;
import backend.greatjourney.domain.course.service.CourseProgressService;
import backend.greatjourney.domain.course.service.CourseService;
import backend.greatjourney.domain.course.service.PlaceService;
import backend.greatjourney.domain.survey.dto.AnswerReq;
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
    private final PlaceService placeService;
    private final CourseProgressService courseProgressService;

    /**
     * GET /api/course-detail?id={courseId}
     */
    @GetMapping("/course-detail")
    public ResponseEntity<BaseResponse<CourseDetailResponse>>  getCourse(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestParam("id") Integer id){

        CourseDetailResponse courseData = courseService.getCourseDetail(id);

        BaseResponse<CourseDetailResponse> response = BaseResponse.<CourseDetailResponse>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("코스 상세 정보 조회에 성공했습니다.")
                .data(courseData)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/place-detail?id={placeId}
     */
    @GetMapping("/place-detail")
    public ResponseEntity<BaseResponse<PlaceDetailResponse>> getPlace(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@RequestParam("id") Long id){
        PlaceDetailResponse placeData = placeService.getPlaceDetail(id);

        BaseResponse<PlaceDetailResponse> response = BaseResponse.<PlaceDetailResponse>builder()
                .isSuccess(true)
                .code(HttpStatus.OK.value())
                .message("장소 상세 정보 조회에 성공했습니다.")
                .data(placeData)
                .build();

        return ResponseEntity.ok(response);
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
    public ResponseEntity<String> endCourse(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @RequestBody @Valid CourseEndReq req) {

        // 로그인된 사용자의 ID와 요청받은 courseId를 서비스로 전달
        courseProgressService.endCourse(customOAuth2User,req);
        return ResponseEntity.ok("코스 진행이 종료되었습니다.");
    }

}