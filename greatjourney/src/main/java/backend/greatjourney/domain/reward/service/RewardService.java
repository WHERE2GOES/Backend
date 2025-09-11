package backend.greatjourney.domain.reward.service;

import java.util.List;

import backend.greatjourney.domain.certification.dto.CourseCertificationStatusResponse;
import backend.greatjourney.domain.certification.service.CertificationService;
import org.springframework.stereotype.Service;

import backend.greatjourney.domain.reward.dto.RewardResponse;
import backend.greatjourney.domain.reward.entity.Reward;
import backend.greatjourney.domain.reward.entity.Rewards;
import backend.greatjourney.domain.reward.repository.RewardRepository;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.domain.user.repository.UserRepository;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardService {

	private final RewardRepository rewardRepository;
	private final UserRepository userRepository;
	private final CertificationService certificationService;

	//사용자가 요청하면 다 완료된 것을 확인해서 발급해주기 -> 백에서 처리?해야할 듯
	public BaseResponse<Void> getReward(CustomOAuth2User customOAuth2User,Long courseId){

		//일단 사용자에 대한 검증이 필요 -> 사용자의 보상 정보 존재 해야함
		if(!userRepository.existsByUserId(Long.parseLong(customOAuth2User.getUserId()))){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}

		User user = userRepository.findByUserId(Long.parseLong(customOAuth2User.getUserId()))
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

		//코스를 다 돌았는지 확인하는 부분이 필요함
		//해당 코스를 다 돌았는지 필요
		// 2. CertificationService를 사용하여 해당 코스를 모두 완료했는지 확인
		CourseCertificationStatusResponse certificationStatus =
				certificationService.getUserCertificationsForCourse(customOAuth2User, courseId.intValue());

		// 만약 코스를 완료 / 미완료
		if (!certificationStatus.isCompleted()) {
//			미완료
//			throw new CustomException(ErrorCode.COURSE_NOT_COMPLETED); // ErrorCode에 COURSE_NOT_COMPLETED 추가 필요
		}
		else{
//			완료
		}

		//어떻게 다 돌았는지 확인

		Reward reward = Reward.builder()
			.courseId(courseId)
			.user(user)
			.build();



		rewardRepository.save(reward);


		//여기에 리턴 값으로 카톡 테마의 주소 같은 것을 보내줄 수 있을 듯
		return BaseResponse
			.<Void>builder()
			.message("보상이 추가되었습니다.")
			.data(null)
			.code(201)
			.build();
	}


	public BaseResponse<RewardResponse> getAllRewards(CustomOAuth2User customOAuth2User){
		if(!userRepository.existsByUserId(Long.parseLong(customOAuth2User.getUserId()))){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}

		User user = userRepository.findByUserId(Long.parseLong(customOAuth2User.getUserId()))
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

		List<RewardResponse.Rewards> rewardsList = rewardRepository.findAllByUser(user).stream()
			.map(reward -> {
				return new RewardResponse.Rewards(reward.getCourseId().toString());
			}).toList();

		return BaseResponse
			.<RewardResponse>builder()
			.message("보상을 조회하였습니다.")
			.data(new RewardResponse(rewardsList))
			.code(200)
			.build();
	}

}
