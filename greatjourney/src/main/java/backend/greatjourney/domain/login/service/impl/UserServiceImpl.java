package backend.greatjourney.domain.login.service.impl;

import backend.greatjourney.domain.login.domain.User;
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
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public User saveUploadImg(String url, String loginId) {
        Optional<User> optionalUser = userRepository.findByEmail(loginId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfileImageUrl(url);

            return userRepository.save(user);
        } else {
            return null;
        }

    }

    @Override
    public User profileSave(ProfileRequest profileRequest, String loginId) {

//        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            user.setNickname(profileRequest.getNickname());
//            user.setIntroduction(profileRequest.getIntroduction());
//            user.setBirth(LocalDate.parse(profileRequest.getBirth()));
//            user.setGender(profileRequest.getGender());
//
//
//            return userRepository.save(user);
//
//        }
        return null;
    }

    @Override
    public Map<String, Object> getProfileInfo(String loginId) {
        return getStringObjectMap(loginId);
    }


    private Map<String, Object> getStringObjectMap(String visitedUserId) {
        Optional<User> optionalUser = userRepository.findByEmail(visitedUserId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Map<String, Object> profileInfo = new HashMap<>();
            profileInfo.put("userId", user.getId());
            profileInfo.put("loginId", user.getNickname());
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
