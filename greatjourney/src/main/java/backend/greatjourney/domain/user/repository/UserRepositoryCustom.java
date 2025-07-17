package backend.greatjourney.domain.user.repository;

import java.util.Optional;

import backend.greatjourney.domain.user.entity.User;

public interface UserRepositoryCustom {

	boolean existsByEmail(String email);
	Optional<User> findByUserId(Long userId);
	Optional<User> findByEmail(String email);
}
