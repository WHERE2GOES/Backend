package backend.greatjourney.chat;

import backend.greatjourney.domain.community.controller.ChatController;
import backend.greatjourney.domain.community.controller.request.ChatRoomRequestDTO;
import backend.greatjourney.domain.community.entity.dto.ChatDTO;
import backend.greatjourney.domain.community.service.CreateChatRoomService;
import backend.greatjourney.global.exception.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateChatRoomService createChatRoomService;

    @MockBean
    private SimpMessagingTemplate msgOperation;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateChatRoom() throws Exception {
        ChatRoomRequestDTO requestDTO = new ChatRoomRequestDTO("receiver", "sender");

        when(createChatRoomService.createChatRoom(any(String.class), any(String.class)))
                .thenReturn(new BaseResponse<Object>(true, "Chat room created",200,null));

        mockMvc.perform(post("/chatting/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEnterChatRoom() throws Exception {
        ChatDTO chatDto = new ChatDTO();
        chatDto.setRoomId("123");

        when(createChatRoomService.enterChatRoom(any(ChatDTO.class), any(SimpMessageHeaderAccessor.class)))
                .thenReturn(chatDto);

        // 이 부분은 WebSocket을 직접 테스트할 때 사용됩니다.
        // RestController를 테스트하는 경우, SimpMessagingTemplate을 직접 모킹하는 것이 아니라 WebSocketConfig에서 설정을 하는 것이 일반적입니다.
        // 만약 REST API로 엔터 기능이 구현되어 있다면, `post`나 `get` 메서드를 사용해야 합니다.

        mockMvc.perform(post("/chat/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendChatRoom() throws Exception {
        ChatDTO chatDto = new ChatDTO();
        chatDto.setRoomId("123");

        // 이 테스트는 WebSocket 기반의 메서드를 호출하는 것으로 보이는데,
        // REST API로 구현된 것이 아니면 `MockMvc`로 테스트하기 어렵습니다.
        // `SimpMessagingTemplate`을 직접 모킹하여 서비스 레이어를 테스트하는 것이 좋습니다.

        mockMvc.perform(post("/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatDto)))
                .andExpect(status().isOk());
    }
}
