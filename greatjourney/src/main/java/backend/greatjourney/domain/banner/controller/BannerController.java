package backend.greatjourney.domain.banner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.greatjourney.domain.banner.service.BannerService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/banner")
public class BannerController {

	private final BannerService bannerService;

	@GetMapping()
	public BaseResponse<List<String>> getBanner(){
		return BaseResponse.<List<String>>builder()
			.isSuccess(true)
			.message("배너들을 가져왔습니다.")
			.code(200)
			.data(bannerService.getBanners())
			.build();
	}
}
