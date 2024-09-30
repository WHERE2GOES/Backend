package backend.greatjourney.domain.authentication.service;


import backend.greatjourney.domain.authentication.controller.dto.StampRequestDTO;
import backend.greatjourney.domain.authentication.controller.dto.StampResponseDTO;
import backend.greatjourney.domain.authentication.repository.StampLocationRepository;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.repository.UserRepository;
import backend.greatjourney.global.hashing.TokenHashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
@Slf4j
@RequiredArgsConstructor
@Service
public class FindStampService {

    private final StampLocationRepository stampLocationRepository;
    private final UserRepository userRepository;
    private final TokenHashing tokenHashing;

    public StampResponseDTO checkUserStamp(StampRequestDTO stampRequestDTO, String token) {

        //헤더에 있는 토큰을 통해서 가져오는 것이다.
        //유저에 대한 정보를 가져옴
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();




        return StampResponseDTO.builder().build();
    }


}
