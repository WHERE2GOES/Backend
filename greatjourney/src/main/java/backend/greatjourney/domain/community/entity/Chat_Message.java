package backend.greatjourney.domain.community.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat_Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //기본 키가 될 id

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; //우혁이가 만든 user를 여기에 넣어주면 된다.

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private Chat_Room chat_room;

    private  String content;

    private String content_type;

    private String content_url;

}
