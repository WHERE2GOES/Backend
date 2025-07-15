package backend.greatjourney.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "User")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private Status status;

	private Domain domain;

	private String email;


	@Builder
	private User(Long id, Status status, Domain domain,String email){
		this.userId = id;
		this.status = status;
		this.domain = domain;
		this.email = email;
	}


}
