package backend.greatjourney.domain.community.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Community_Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; //여기에도 만들어진 user 사용

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Posting posting;

    private String content;

}
