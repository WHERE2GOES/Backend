package backend.greatjourney.domain.community.entity.dto;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDTO {

    //메시지 타입
    public enum MessageType {
        ENTER,
        TALK,
        LEAVE;
    }

    private MessageType messageType;
    private String sender;
    private String roomId;
    private String message;
}
