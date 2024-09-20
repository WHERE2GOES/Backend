package backend.greatjourney.domain.login.service.impl;

import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.dto.ProfileEditRequest;
import backend.greatjourney.domain.login.dto.ProfileRequest;
import backend.greatjourney.domain.login.repository.UserRepository;
import backend.greatjourney.domain.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String userEmail) {
                return userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User saveUploadImg(String url, Long loginId) {
        Optional<User> optionalUser = userRepository.findById(loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfileImageUrl(url);

            return userRepository.save(user);
        } else {
            return null;
        }

    }

    @Override
    public User profileSave(ProfileEditRequest profileRequest, Long loginId) {

        Optional<User> optionalUser = userRepository.findById(loginId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setNickname(profileRequest.getNickname());
            user.setResidence(profileRequest.getResidence());
            user.setGender(profileRequest.isGender());
            user.setPhone(profileRequest.getPhone());

            return userRepository.save(user);

        }
        return null;
    }

    @Override
    public Map<String, Object> getProfileInfo(Long loginId) {
        return getStringObjectMap(loginId);
    }


    private Map<String, Object> getStringObjectMap(Long visitedUserId) {
        Optional<User> optionalUser = userRepository.findById(visitedUserId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Map<String, Object> profileInfo = new HashMap<>();
//            profileInfo.put("userId", user.getId());
            profileInfo.put("nickname", user.getNickname());
//            profileInfo.put("password", user.getPassword());
            profileInfo.put("Email", user.getEmail());
            profileInfo.put("Gender", user.isGender());
            profileInfo.put("Role", user.getRole());
            profileInfo.put("ProfileImg", user.getProfileImageUrl());

            return profileInfo;
        } else {
            return Collections.emptyMap();
        }
    }

    public User getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        return user;
    }
}
