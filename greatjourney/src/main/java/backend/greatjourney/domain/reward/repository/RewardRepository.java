package backend.greatjourney.domain.reward.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.greatjourney.domain.reward.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward,Long > , RewardRepositoryCustom{
}
