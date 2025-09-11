package backend.greatjourney.domain.reward.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import backend.greatjourney.domain.reward.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward,Long > , RewardRepositoryCustom{

	List<Reward> findAllByUser(backend.greatjourney.domain.user.entity.User user);

}
