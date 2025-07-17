package backend.greatjourney.domain.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.greatjourney.domain.token.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

	Void deleteByToken(String token);
	RefreshToken findByUserId(Long userId);
}
