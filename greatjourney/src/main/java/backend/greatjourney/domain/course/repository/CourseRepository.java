package backend.greatjourney.domain.course.repository;

import backend.greatjourney.domain.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findAllByIdIn(List<Integer> ids);
}