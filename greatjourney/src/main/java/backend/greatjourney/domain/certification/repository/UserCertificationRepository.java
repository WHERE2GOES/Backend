package backend.greatjourney.domain.certification.repository;

import backend.greatjourney.domain.certification.domain.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCertificationRepository extends JpaRepository<UserCertification, Long> {
}
