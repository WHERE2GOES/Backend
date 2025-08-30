package backend.greatjourney.domain.region.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.greatjourney.domain.region.entity.Region;

public interface RegionRepository extends JpaRepository<Region,Long> {

	Optional<Region> findByAreaNmAndSigunguNm(String areaNm,String sigunguNm);
}
