package backend.greatjourney.domain.login.converter;

import backend.greatjourney.domain.login.Role;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.SignUpRequest;
import jakarta.persistence.*;

public class SignupConverter {
    public static User toUser(SignUpRequest signUpRequest, Role role) {
        return User.builder()
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .residence(signUpRequest.getResidence())
                .gender(signUpRequest.isGender())
                .phone(signUpRequest.getPhone())
                .sns(signUpRequest.isSns())
                .role(role)
                .build();
    }
}
