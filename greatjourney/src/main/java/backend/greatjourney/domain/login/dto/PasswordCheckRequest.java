package backend.greatjourney.domain.login.dto;

import lombok.Data;

@Data
public class PasswordCheckRequest {
    private String firstPassword;
    private String secondPassword;
}
