package backend.greatjourney.domain.certification.repository;

import backend.greatjourney.domain.certification.domain.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCertificationRepository extends JpaRepository<UserCertification, Long> {
    List<UserCertification> findByUser_UserIdAndCourseId(Long userId, Integer courseId);
}
