package backend.greatjourney.domain.reward.dto;

import java.util.List;

public record RewardResponse(List<Rewards> rewardsList) {
	public record  Rewards(String courseId){}
}
