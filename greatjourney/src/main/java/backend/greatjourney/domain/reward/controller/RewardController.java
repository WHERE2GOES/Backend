package backend.greatjourney.domain.reward.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.reward.entity.Rewards;
import backend.greatjourney.domain.reward.service.RewardService;
import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.security.entitiy.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reward")
@RequiredArgsConstructor
public class RewardController {

	private final RewardService rewardService;

	@PostMapping("")
	@Operation(summary = "사용자에게 보상 추가 하는 API입니다.\n" + "보상의 경우 어떠한 보상인지 ex) 어떤 경로 보상인지 현재는 COURSE1, COURSE2, COURSE3 이런 식으로 입력받게 해둠")
	public BaseResponse<Void> makeReward(@AuthenticationPrincipal  CustomOAuth2User customOAuth2User, @RequestParam
		Rewards rewards){
		return rewardService.getReward(customOAuth2User,rewards);
	}

}
