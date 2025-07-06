package backend.greatjourney.domain.course.repository;

import backend.greatjourney.domain.course.domain.CoursePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursePointRepository extends JpaRepository<CoursePoint, Long> {
    List<CoursePoint> findByCourse_IdOrderBySeqAsc(Integer courseId);
}