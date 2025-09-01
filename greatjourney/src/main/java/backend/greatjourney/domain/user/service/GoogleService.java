package backend.greatjourney.domain.user.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.greatjourney.domain.token.dto.TokenResponse;
import backend.greatjourney.domain.token.service.JwtTokenProvider;
import backend.greatjourney.domain.user.dto.response.GoogleUserResponse;
import backend.greatjourney.domain.user.dto.response.KakaoUserResponse;
import backend.greatjourney.domain.user.entity.User;
import backend.greatjourney.global.exception.CustomException;
import backend.greatjourney.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService {

	private final RestTemplate restTemplate = new RestTemplate();

	private final JwtTokenProvider jwtTokenProvider;
	private final SignService signService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String domain = "GOOGLE";


	public TokenResponse loginWithGoogle(String accessToken){
		GoogleUserResponse googleUserResponse = getUserInfo(accessToken);
		User user = signService.saveUser(googleUserResponse.getEmail(),domain);
		return jwtTokenProvider.createToken(user.getUserId().toString());
	}




	private GoogleUserResponse getUserInfo(String accessToken) {
		String url = "https://www.googleapis.com/oauth2/v2/userinfo";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {

				log.info(response.getBody());
				return objectMapper.readValue(response.getBody(), GoogleUserResponse.class);
			} else {
				throw new CustomException(ErrorCode.USER_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new CustomException(ErrorCode.KAKAO_USER_ERROR);
		}
	}



}
