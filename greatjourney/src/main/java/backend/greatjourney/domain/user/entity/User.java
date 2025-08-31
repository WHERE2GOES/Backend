package backend.greatjourney.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "User")
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;

	@Setter
	private String name;

	@Setter
	private Status status;

	private Domain domain;

	private String email;

	@Setter
	private UserRole userRole;

	private Terms terms;



	@Builder
	private User(Long id, Status status, Domain domain,String email,UserRole userRole, Terms terms,String name){
		this.userId = id;
		this.status = status;
		this.domain = domain;
		this.name = name;
		this.email = email;
		this.userRole = userRole;
		this.terms = terms;
	}


	@Embeddable
	@Getter
	@NoArgsConstructor
	public static class Terms {
		private boolean marketing;

		@Builder
		private Terms(boolean marketing) {
			this.marketing = marketing;
		}
	}

}
