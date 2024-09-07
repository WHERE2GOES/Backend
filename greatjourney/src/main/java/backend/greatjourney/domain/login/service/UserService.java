package backend.greatjourney.domain.login.service;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.ProfileRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserService {

    UserDetailsService userDetailsService();

    User profileSave(ProfileRequest profileRequest, String loginId);

    Map<String, Object> getProfileInfo(String loginId);

    User saveUploadImg(String file, String loginId);
}

