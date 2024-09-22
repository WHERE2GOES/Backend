package backend.greatjourney.domain.community.entity;

import backend.greatjourney.domain.base_time.BaseTimeEntity;
import backend.greatjourney.domain.login.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Posting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    //기본 키가 될 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
    //user와 연결되어 접속할 수 있도록 한다.

    //mappedBy
    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Community_Comment> communityComments;

    private String image_url;

    private String title;

    private String contents;

    private String location;

    private Long view;

    //조회수를 증가시켜주기 위한 함수
    public void updateView(Long view){
        this.view = view;
    }

}
