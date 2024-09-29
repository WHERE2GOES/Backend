package backend.greatjourney.domain.facility.repository;

import backend.greatjourney.domain.facility.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByRouteId(Integer routeId);
}