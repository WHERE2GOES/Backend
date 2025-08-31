package backend.greatjourney.domain.user.service;

import org.apache.xmlbeans.impl.store.DomImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.greatjourney.domain.user.dto.response.GoogleUserResponse;
import backend.greatjourney.domain.user.dto.response.KakaoUserResponse;
import backend.greatjourney.domain.user.dto.response.LoginErrorResponse;
import backend.greatjourney.domain.user.entity.Domain;
import backend.greatjourney.domain.user.entity.Status;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.entity.UserRole;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.exception.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignService {

	private final UserRepository userRepository;

	@Transactional(noRollbackFor = LoginException.class)
	public User saveUser(String email, String domain){

		if(!userRepository.existsByEmail(email)){
			Domain realDomain = Domain.valueOf(domain);
			User user = User.builder()
				.userRole(UserRole.ROLE_USER)
				.domain(realDomain)
				.email(email)
				.status(Status.PENDING)
				.build();

			userRepository.save(user);

			throw new LoginException(HttpStatus.NOT_FOUND,404,"유저가 존재하지 않습니다. 회원가입이 필요합니다.",new LoginErrorResponse(email,domain));
		}


		return userRepository.findByEmail(email)
			.orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

	}


}
