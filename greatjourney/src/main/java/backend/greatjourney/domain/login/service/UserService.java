package backend.greatjourney.domain.login.service;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.ProfileEditRequest;
import backend.greatjourney.domain.login.dto.ProfileRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService {

    UserDetailsService userDetailsService();

    User profileSave(ProfileEditRequest profileRequest, Long loginId);

    Map<String, Object> getProfileInfo(Long loginId);

    User saveUploadImg(String file, Long loginId);
}

