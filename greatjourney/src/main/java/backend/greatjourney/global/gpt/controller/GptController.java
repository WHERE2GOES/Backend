package backend.greatjourney.global.gpt.controller;

import org.checkerframework.common.util.count.report.qual.ReportUnqualified;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.global.exception.BaseResponse;
import backend.greatjourney.global.gpt.dto.GptRankResponse;
import backend.greatjourney.global.gpt.dto.GptRequest;
import backend.greatjourney.global.gpt.dto.GptResponse;
import backend.greatjourney.global.gpt.service.GptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
@Tag(name = "GPT API", description = "GPT에게 요청 보내는 API입니다.")
public class GptController {
	private final GptService gptService;

	//
	// @PostMapping("")
	// @Operation(summary = "GPT한테 텍스트로 요청보내는 API입니다.")
	// public BaseResponse<GptRankResponse> askGPT(){
	// 	return BaseResponse.<GptRankResponse>builder()
	// 		.code(200)
	// 		.isSuccess(true)
	// 		.message("gpt 답변입니다.")
	// 		.data(gptService.rankBikeTrails())
	// 		.build();
	// }


	@GetMapping("/bike-trails")
	@Operation(summary = "추천 TOP3 자전거길 받는 API입니다.")
	public GptRankResponse get() {
		return gptService.rankBikeTrails();
	}

	@PostMapping("/bike-trails/refresh")
	@Operation(summary = "추천 TOP3 자전거길 새로고침하는 API입니다.")
	public GptRankResponse refresh(@RequestParam Long seed) {
		return gptService.refreshBikeTrailsRandom(0.6,seed);
	}

	@DeleteMapping("/bike-trails/cache")
	@Operation(summary = "지금까지 있는 TOP3 자전거기를 삭제하는 API입니다.")
	public void evict() {
		gptService.evictBikeTrails();
	}
}
