package backend.greatjourney.domain.token.entity;

import java.time.Instant;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RefreshToken {

	@Id
	private String tokenId;
	private Long userId;
	private String token;
	private Instant expiryDate;

	@Builder
	private RefreshToken(Long userId, String id, String token, Instant expiryDate) {
		this.userId = userId;
		this.tokenId = id;
		this.token = token;
		this.expiryDate = expiryDate;
	}
}
