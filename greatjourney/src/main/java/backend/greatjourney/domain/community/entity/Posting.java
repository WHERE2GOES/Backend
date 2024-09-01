package backend.greatjourney.domain.community.entity;

import backend.greatjourney.domain.base_time.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Posting extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //기본 키가 될 id

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; //우혁이가 만든 user를 여기에 넣어주면 된다.

    private String image_url;

    private String title;

    private String nickname;

    private String contents;

    private String location;

    private Long view;

}
