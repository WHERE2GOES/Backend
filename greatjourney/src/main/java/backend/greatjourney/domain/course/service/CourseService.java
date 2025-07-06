package backend.greatjourney.domain.course.service;

import backend.greatjourney.domain.course.domain.CoursePoint;
import backend.greatjourney.domain.course.domain.Place;
import backend.greatjourney.domain.course.dto.CourseDetailResponse;
import backend.greatjourney.domain.course.dto.LatLngDto;
import backend.greatjourney.domain.course.dto.PlaceItemDto;
import backend.greatjourney.domain.course.repository.CoursePointRepository;
import backend.greatjourney.domain.course.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CoursePointRepository pointRepo;
    private final PlaceRepository placeRepo;

    public CourseDetailResponse getCourseDetail(Integer courseId){
        List<CoursePoint> points = pointRepo.findByCourse_IdOrderBySeqAsc(courseId);
        if(points.isEmpty()) throw new IllegalArgumentException("No such course " + courseId);

        List<LatLngDto> route = points.stream()
                .map(p -> new LatLngDto(p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());

        // bounding‑box 구해서 근처 place 조회 (±0.02° buffer)
        DoubleSummaryStatistics latStat = points.stream().mapToDouble(CoursePoint::getLatitude).summaryStatistics();
        DoubleSummaryStatistics lonStat = points.stream().mapToDouble(CoursePoint::getLongitude).summaryStatistics();
        double margin = 0.02;
        List<Place> boxes = placeRepo.findInBoundingBox(latStat.getMin()-margin, latStat.getMax()+margin,
                lonStat.getMin()-margin, lonStat.getMax()+margin);
        List<PlaceItemDto> places = boxes.stream()
                .map(p -> new PlaceItemDto(p.getId(), p.getCategory(), p.getLatitude(), p.getLongitude()))
                .collect(Collectors.toList());
        return new CourseDetailResponse(route, places);
    }
}