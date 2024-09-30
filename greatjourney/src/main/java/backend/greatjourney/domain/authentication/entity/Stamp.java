package backend.greatjourney.domain.authentication.entity;


import backend.greatjourney.domain.login.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Stamp {

    //인증과 관련된 entity
    @Id
    private Long id;

    private String stampLocation;

    //이렇게 user와 연관지어 준다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;


}
