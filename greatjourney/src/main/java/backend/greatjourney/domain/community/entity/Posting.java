package backend.greatjourney.domain.community.entity;

import backend.greatjourney.domain.base_time.BaseTimeEntity;
import backend.greatjourney.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Posting extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //기본 키가 될 id

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    //user와 연결되어 접속할 수 있도록 한다.

    private String image_url;

    private String title;

    private String nickname;

    private String contents;

    private String location;

    private Long view;

}
