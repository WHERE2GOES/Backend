package backend.greatjourney.domain.user.service;

import org.apache.xmlbeans.impl.store.DomImpl;
import org.springframework.stereotype.Service;

import backend.greatjourney.domain.user.dto.response.GoogleUserResponse;
import backend.greatjourney.domain.user.dto.response.KakaoUserResponse;
import backend.greatjourney.domain.user.entity.Domain;
import backend.greatjourney.domain.user.entity.Status;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.entity.UserRole;
import backend.greatjourney.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignService {

	private final UserRepository userRepository;

	public User saveUserKakao(KakaoUserResponse userInfo, String domain){

		Domain realDomain = Domain.valueOf(domain);

		User user = User.builder()
			.userRole(UserRole.ROLE_USER)
			.domain(realDomain)
			.email(userInfo.getKakao_account().getEmail())
			.name(userInfo.getProperties().getNickname())
			.status(Status.PENDING)
			.build();

		return userRepository.save(user);

	}

	public User saveUserGoogle(GoogleUserResponse userInfo, String domain){

		Domain realDomain = Domain.valueOf(domain);

		User user = User.builder()
			.userRole(UserRole.ROLE_USER)
			.domain(realDomain)
			.email(userInfo.getEmail())
			.status(Status.PENDING)
			.build();

		return userRepository.save(user);

	}




}
