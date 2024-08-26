package backend.greatjourney.domain.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String residence;

    private boolean gender;

    private String phone;

    private boolean sns;

    private boolean snsId;

}
