package backend.greatjourney.domain.course.repository;

import backend.greatjourney.domain.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> { }