package backend.greatjourney.domain.community.entity;


import backend.greatjourney.domain.login.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //기본 키가 될 id
    private String roomId;

    @Column(name = "host")
    private String host;

    @Column(name = "guest")
    private String guest;

    @Column(name="room_name")
    private String room_name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; //우혁이가 만든 user를 여기에 넣어주면 된다.

    @OneToMany(mappedBy = "chat_room", cascade = CascadeType.ALL)
    List<Chat_Message> chatMessageList = new ArrayList<>();


    //of 메소드 구현
    public static Chat_Room of(String host, String guest) {
        return Chat_Room.builder()
                .roomId(UUID.randomUUID().toString())
                .room_name(host + "님과의 대화 ο(=•ω＜=)ρ⌒☆")
                .host(host)
                .guest(guest)
                .build();
    }

}
