package backend.greatjourney.domain.authentication.repository;

import backend.greatjourney.domain.authentication.entity.StampLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StampLocationRepository  extends JpaRepository<StampLocation, Long> {
    StampLocation findStampLocationByLocationName(String locationName);


}
