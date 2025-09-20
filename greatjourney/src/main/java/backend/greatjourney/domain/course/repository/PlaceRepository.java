package backend.greatjourney.domain.course.repository;


import backend.greatjourney.domain.course.domain.Place;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p WHERE p.latitude BETWEEN :minLat AND :maxLat AND p.longitude BETWEEN :minLon AND :maxLon")
    List<Place> findInBoundingBox(@Param("minLat") double minLat,
                                  @Param("maxLat") double maxLat,
                                  @Param("minLon") double minLon,
                                  @Param("maxLon") double maxLon);

    List<Place> findByCategoryNotAndName(String category, String name);

    List<Place> findByCategoryAndCourseId(String category, Integer courseId);

    long countByCategoryAndCourseId(String category, Integer courseId);

    List<Place> findAllByCategory(String category);


}