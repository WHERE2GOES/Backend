package backend.greatjourney.domain.survey.repository;

import backend.greatjourney.domain.course.domain.Course;
import backend.greatjourney.domain.survey.domain.CourseCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseCriteriaRepository extends JpaRepository<CourseCriteria, Long> {

    @Query("SELECT cc.courseId FROM CourseCriteria cc WHERE cc.difficulty = :difficulty AND cc.scenery = :scenery AND cc.geography = :geography")
    List<Integer> findCourseIdsByCriteria(String difficulty, String scenery, String geography);

}