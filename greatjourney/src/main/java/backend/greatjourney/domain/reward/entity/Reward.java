package backend.greatjourney.domain.reward.entity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import backend.greatjourney.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Reward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Long courseId;

	private String rewardItem;


	@Builder
	private Reward(User user, Long courseId,String rewardItem){
		this.user = user;
		this.courseId = courseId;
		this.rewardItem = rewardItem;
	}

}
