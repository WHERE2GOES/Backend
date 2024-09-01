package backend.greatjourney.domain.community.service;


import backend.greatjourney.domain.community.repository.Chat_MessageRepository;
import backend.greatjourney.domain.community.repository.Chat_RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatChatRoomService {

    private final Chat_MessageRepository chatMessageRepository;
    private final Chat_RoomRepository chatRoomRepository;







}
