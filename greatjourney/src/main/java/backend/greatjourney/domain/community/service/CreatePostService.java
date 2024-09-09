package backend.greatjourney.domain.community.service;


import backend.greatjourney.domain.community.controller.request.PostRequestDTO;
import backend.greatjourney.domain.community.controller.response.PostResponseDTO;
import backend.greatjourney.domain.community.repository.Chat_MessageRepository;
import backend.greatjourney.domain.community.repository.Chat_RoomRepository;
import backend.greatjourney.domain.community.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePostService {

    private final PostingRepository postingRepository;

    public PostResponseDTO makePost(@RequestBody PostRequestDTO postRequestDTO) {



        return PostResponseDTO.builder()
                .user(postRequestDTO.getUser())
                .build();
    }



}
