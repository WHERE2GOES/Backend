package backend.greatjourney.domain.user.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import aj.org.objectweb.asm.TypeReference;
import backend.greatjourney.domain.token.dto.TokenResponse;
import backend.greatjourney.domain.token.service.JwtTokenProvider;
import backend.greatjourney.domain.user.dto.properties.KakaoOAuthProperties;
import backend.greatjourney.domain.user.dto.response.KakaoUserResponse;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.global.exception.BaseException;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final KakaoOAuthProperties kakaoProps;
	private final JwtTokenProvider jwtTokenProvider;
	private final SignService signService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String domain = "kakao";

	public TokenResponse loginWithKakao(String accessToken) {
		KakaoUserResponse userInfo = getUserInfo(accessToken);
		User user = signService.saveUserKakao(userInfo, domain);
		return jwtTokenProvider.createToken(user.getUserId().toString());
	}


	private KakaoUserResponse getUserInfo(String accessToken) {
		String url = "https://kapi.kakao.com/v2/user/me";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				return objectMapper.readValue(response.getBody(), KakaoUserResponse.class);
			} else {
				throw new CustomException(ErrorCode.USER_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new CustomException(ErrorCode.KAKAO_USER_ERROR);
		}
	}
}
