package backend.greatjourney.domain.community.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDTO {

    private String sender;
    private String receiver;

}
