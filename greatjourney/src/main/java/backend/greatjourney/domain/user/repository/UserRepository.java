package backend.greatjourney.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.greatjourney.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User,Long> , UserRepositoryCustom {
}
