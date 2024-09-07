package backend.greatjourney.domain.login.dto;

import lombok.Data;

@Data
public class KakaoSigninRequest {

    private String loginId;
    private String nickname;

}
