package backend.greatjourney.global.gpt.controller;

import org.checkerframework.common.util.count.report.qual.ReportUnqualified;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.global.exception.BaseResponse;
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


	@PostMapping("")
	@Operation(summary = "GPT한테 텍스트로 요청보내는 API입니다.")
	public BaseResponse<GptResponse> askGPT(@RequestBody GptRequest gptRequest){
		return BaseResponse.<GptResponse>builder()
			.code(200)
			.isSuccess(true)
			.message("gpt 답변입니다.")
			.data(gptService.askGpt(gptRequest.question()))
			.build();
	}
}
