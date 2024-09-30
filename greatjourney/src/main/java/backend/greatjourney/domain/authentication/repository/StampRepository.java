package backend.greatjourney.domain.authentication.repository;

import backend.greatjourney.domain.authentication.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
}
