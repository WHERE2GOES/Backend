//package backend.greatjourney.domain.community.controller;
//
//
//import backend.greatjourney.domain.community.controller.response.ResponseDTO;
//import backend.greatjourney.domain.community.entity.dto.ChatDTO;
//import backend.greatjourney.domain.community.service.CreateChatRoomService;
//import backend.greatjourney.global.exception.BaseResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/chatting")
//public class ChatController {
//
//    private final CreateChatRoomService createChatRoomService;
//    //이렇게 하는게 연결하는 것이다.
//    private final SimpMessagingTemplate msgOperation;
//
//    @PostMapping("/chat")
//    public BaseResponse<Object> createChatRoom(@RequestBody String receiver, String sender) {
//        // @Param sender should be replaced to UserDetails.getMember();
////        return createChatRoomService.createChatRoom(receiver, sender);
//        return createChatRoomService.createChatRoom(receiver,sender);
//        //이렇게 BaseResponse로 리턴하면 될듯
//        // createChatRoom의 결과인 roomId와 type : ENTER을 저장한 chatDto에 넣어줘야함
//    }
//
//    //여기서 roomId를 보통 어떤 값을 가져오게 되는 것이지? 제대로 된 아이디 값을 가져오면 좋을텐데
//    @MessageMapping("/chat/enter")
////    @SendTo("/sub/chat/{roomId}")
//    public void enterChatRoom(ChatDTO chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        Thread.sleep(500); // simulated delay
//        ChatDTO newchatdto = createChatRoomService.enterChatRoom(chatDto, headerAccessor);
//        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), newchatdto);
//    }
//
//    @MessageMapping("/chat/send")
////    @SendTo("/sub/chat/{roomId}")
//    public void sendChatRoom(ChatDTO chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        Thread.sleep(50); // simulated delay
//        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
//    }
//
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        ChatDTO chatDto = CreateChatRoomService.disconnectChatRoom(headerAccessor);
//        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
//    }
//
//
//}
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
@RequestMapping("/chatting")
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
