package backend.greatjourney.domain.reward.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.greatjourney.domain.reward.entity.RewardList;

public interface RewardListRepository extends JpaRepository<RewardList,Long> {
	Optional<RewardList> findByCourseId(Long id);
}
