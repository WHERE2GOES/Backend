package backend.greatjourney.domain.user.service;

import org.springframework.stereotype.Service;

import backend.greatjourney.domain.user.dto.SignUpRequest;
import backend.greatjourney.domain.user.entity.Domain;
import backend.greatjourney.domain.user.entity.Status;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;


	public BaseResponse<User> signupUser(SignUpRequest request){

		if(userRepository.existsByEmail(request.email())){
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		}
		return new BaseResponse<>(true,"회원가입이 완료되었습니다",201,userRepository.save(User.builder()
			.domain(Domain.valueOf(request.domain()))
			.email(request.email())
			.status(Status.SUCCESS)
			.build()));


	}


}
