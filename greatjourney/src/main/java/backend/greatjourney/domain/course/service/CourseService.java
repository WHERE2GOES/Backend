package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.domain.Course;
import backend.greatjourney.domain.course.domain.CoursePoint;
import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.dto.*;
import backend.greatjourney.domain.course.repository.CoursePointRepository;
import backend.greatjourney.domain.course.repository.CourseRepository;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CoursePointRepository pointRepo;
    private final PlaceRepository placeRepo;
    private final CourseRepository courseRepository;

    public CourseMasterResponse getCourseMaster(Integer courseId) {
        List<CoursePoint> points = getPointsForCourse(courseId);
        List<LatLngDto> route = points.stream()
                .map(p -> new LatLngDto(p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());
        List<PlaceItemDto> places = findCombinedPlacesForCourse(courseId, points);
        return new CourseMasterResponse(route, places);
    }


    public CourseRouteResponse getCourseRoute(Integer courseId) {
        List<CoursePoint> points = getPointsForCourse(courseId);

        List<LatLngDto> route = points.stream()
                .map(p -> new LatLngDto(p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());

        return new CourseRouteResponse(route);
    }

    public CoursePlacesResponse getCoursePlaces(Integer courseId) {
        List<CoursePoint> points = getPointsForCourse(courseId);
        List<PlaceItemDto> places = findCombinedPlacesForCourse(courseId, points);
        return new CoursePlacesResponse(places);
    }

    private List<PlaceItemDto> findCombinedPlacesForCourse(Integer courseId, List<CoursePoint> points) {
        // --- 1. '인증센터' 조회 (course_id 기반) ---
        // 이 로직은 기존과 동일합니다.
        List<Place> certificationCenters = placeRepo.findByCategoryAndCourseId("인증센터", courseId);

        // --- 2. '인증센터 외 장소' 조회 (Course와 Place의 name 일치 기반) ---
        // a. courseId로 Course 엔티티를 찾아 이름을 가져옵니다.
        Course currentCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.COURSE_NOT_FOUND));
        String courseName = currentCourse.getName();

        // b. Repository에 추가한 메서드를 호출하여 이름이 같은 다른 장소들을 찾습니다.
        List<Place> otherPlacesWithNameMatch = placeRepo.findByCategoryNotAndName("인증센터", courseName);

        // --- 3. 두 목록을 합치고 DTO로 변환 ---
        List<Place> finalPlaceList = Stream.concat(certificationCenters.stream(), otherPlacesWithNameMatch.stream())
                .distinct()
                .toList();

        return finalPlaceList.stream()
                .map(p -> new PlaceItemDto(p.getId(), p.getName(), p.getCategory(), p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());
    }

    // 코드 중복을 막기 위한 private 헬퍼 메서드
    private List<CoursePoint> getPointsForCourse(Integer courseId) {
        List<CoursePoint> points = pointRepo.findByCourse_IdOrderBySeqAsc(courseId);
        if (points.isEmpty()) {
            throw new CustomException(ErrorCode.COURSE_NOT_FOUND);
        }
        return points;
    }

}
