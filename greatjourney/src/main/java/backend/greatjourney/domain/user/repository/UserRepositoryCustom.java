package backend.greatjourney.domain.user.repository;

public interface UserRepositoryCustom {

	boolean existsByEmail(String email);
}
