package backend.greatjourney.domain.login.repository;

import backend.greatjourney.domain.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  
    //Boolean existsByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    //Optional<User> findByLoginId(String loginId); // jwt에서 사용


}
