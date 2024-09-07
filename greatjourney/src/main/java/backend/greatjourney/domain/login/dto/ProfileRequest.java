package backend.greatjourney.domain.login.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String nickname;        // 필수
    private String introduction;    // 필수
    private String birth;           // 선택
    private Character gender;          // 선택


}
