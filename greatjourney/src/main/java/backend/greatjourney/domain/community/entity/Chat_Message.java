package backend.greatjourney.domain.community.entity;


import backend.greatjourney.domain.base_time.BaseTimeEntity;
import backend.greatjourney.domain.user.domain.User;
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
public class Chat_Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //기본 키가 될 id

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; //우혁이가 만든 user를 여기에 넣어주면 된다.
    //이게 살짝 sender와 같은 역할이긴 하지

    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private Chat_Room chat_room;

    private  String content;

    private String content_type;

    private String content_url;

}
