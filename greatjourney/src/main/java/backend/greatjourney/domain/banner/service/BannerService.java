package backend.greatjourney.domain.banner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BannerService {

	public List<String> getBanners(){
		List<String> lists = new ArrayList<>();

		lists.add("https://ddo123.s3.ap-northeast-2.amazonaws.com/test_images/a9a4dd36-8572-42f0-a2a1-07fbc3d2c8d8_banner2.png");
		return lists;
	}
}
