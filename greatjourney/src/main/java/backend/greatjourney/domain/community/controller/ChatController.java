
package backend.greatjourney.domain.community.controller;

import backend.greatjourney.domain.community.controller.request.ChatRoomRequestDTO;
import backend.greatjourney.domain.community.entity.dto.ChatDTO;
import backend.greatjourney.domain.community.service.CreateChatRoomService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/chatting")
public class ChatController {

    private final CreateChatRoomService createChatRoomService;
    private final SimpMessagingTemplate msgOperation;

    @PostMapping("/chat")
    public BaseResponse<Object> createChatRoom(@RequestBody ChatRoomRequestDTO request) {
        String receiver = request.getReceiver();
        String sender = request.getSender(); // UserDetails에서 가져오는 부분을 구현
        return createChatRoomService.createChatRoom(receiver, sender);
    }

    @MessageMapping("/chat/enter")
    public void enterChatRoom(ChatDTO chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        ChatDTO newchatdto = createChatRoomService.enterChatRoom(chatDto, headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), newchatdto);
    }

    @MessageMapping("/chat/send")
    public void sendChatRoom(ChatDTO chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        ChatDTO chatDto = createChatRoomService.disconnectChatRoom(headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }
}
