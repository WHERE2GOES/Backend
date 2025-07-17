package backend.greatjourney.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.greatjourney.domain.token.entity.RefreshToken;
import backend.greatjourney.domain.token.repository.RefreshTokenRepository;
import backend.greatjourney.domain.token.service.JwtTokenProvider;
import backend.greatjourney.domain.user.dto.request.ChangeUserRequest;
import backend.greatjourney.domain.user.dto.request.SignUpRequest;
import backend.greatjourney.domain.user.entity.Domain;
import backend.greatjourney.domain.user.entity.Status;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider tokenProvider;

	@Transactional
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

	@Transactional
	public BaseResponse<Void> logOutUser(CustomOAuth2User customOAuth2User){

		Long userId = Long.parseLong(customOAuth2User.getUserId());
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);

		return BaseResponse.<Void>builder()
			.code(200)
			.message("로그아웃이 완료되었습니다.")
			.data(refreshTokenRepository.deleteByToken(refreshToken.getToken()))
			.isSuccess(true)
			.build();
	}


	@Transactional
	public BaseResponse<Void> signOutUser(CustomOAuth2User customOAuth2User){
		Long userId = Long.parseLong(customOAuth2User.getUserId());
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);

		refreshTokenRepository.deleteByToken(refreshToken.getToken());
		User user = userRepository.findByUserId(userId)
			.orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

		user.setStatus(Status.DELETED);
		userRepository.save(user);

		return BaseResponse.<Void>builder()
			.isSuccess(true)
			.code(200)
			.message("회원탈퇴가 완료되었습니다.")
			.data(null)
			.build();
	}

	@Transactional
	public BaseResponse<Void> changeUserInfo(CustomOAuth2User customOAuth2User, ChangeUserRequest request){
		Long userId = Long.parseLong(customOAuth2User.getUserId());
		User user = userRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

		user.setName(request.name());
		userRepository.save(user);
		return BaseResponse.<Void>builder()
			.isSuccess(true)
			.code(200)
			.message("회원정보 수정이 완료되었습니다.")
			.data(null)
			.build();
	}


}
