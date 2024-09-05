package backend.greatjourney.domain.community.service;


import backend.greatjourney.domain.community.controller.response.ResponseDTO;
import backend.greatjourney.domain.community.entity.Chat_Room;
import backend.greatjourney.domain.community.entity.dto.ChatDTO;
import backend.greatjourney.domain.community.repository.Chat_MessageRepository;
import backend.greatjourney.domain.community.repository.Chat_RoomRepository;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateChatRoomService {

    private final Chat_MessageRepository chatMessageRepository;
    private final Chat_RoomRepository chatRoomRepository;

    public BaseResponse<Object> createChatRoom(String receiver, String sender) {
        //이미 reciever와 sender로 생성된 채팅방이 있는지 확인

        //optional이라는건 null일 수도 있는 것을 가정하는 것이다.
        Optional<Chat_Room> findChatRoom = validExistChatRoom(receiver, sender);
        //있으면 ChatRoom의 roomId 반환
        if(findChatRoom.isPresent())
            return ResponseDTO.setSuccess("already has room and find Chatting Room Success!");

        Chat_Room newChatRoom = Chat_Room.of(receiver, sender);
        chatRoomRepository.save(newChatRoom);
        return ResponseDTO.setSuccess("create ChatRoom success");
    }

    public ChatDTO enterChatRoom(ChatDTO chatDto, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 찾기
        Chat_Room chatRoom = validExistChatRoom(chatDto.getRoomId());
        // 예외처리
        //반환 결과를 socket session에 사용자의 id로 저장
        headerAccessor.getSessionAttributes().put("nickname", chatDto.getSender());
        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());

        chatDto.setMessage(chatDto.getSender() + "님이 입장하셨습니다.");
        return chatDto;
    }

    public ChatDTO disconnectChatRoom(SimpMessageHeaderAccessor headerAccessor) {
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String nickName = (String) headerAccessor.getSessionAttributes().get("nickname");

//        chatRoomRepository.deleteByRoomId(roomId);

        ChatDTO chatDto = ChatDTO.builder()
                .messageType(ChatDTO.MessageType.LEAVE)
                .roomId(roomId)
                .sender(nickName)
                .message(nickName + "님이 퇴장하셨습니다.")
                .build();

        return chatDto;
    }

    public Optional<Chat_Room> validExistChatRoom(String host, String guest) {
        return chatRoomRepository.findByHostAndGuest(host, guest);
    }

    public Chat_Room validExistChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                ()-> new NoSuchElementException("채팅방이 존재하지 않습니다.")
        );
    }


}
