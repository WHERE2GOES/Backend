package backend.greatjourney.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	LOGIN_FAIL(HttpStatus.BAD_REQUEST,400,"로그인에 오류가 발생하였습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500,"서버에 오류가 발생하였습니다."),
	JWT_KEY_GENERATION_FAILED(HttpStatus.BAD_REQUEST,400,"JWT 키 생성에 실패하였습니다."),
	NO_REFRESH_TOKEN(UNAUTHORIZED,400, "리프레시 토큰이 없습니다."),
	LOGOUT_ERROR(BAD_REQUEST,400,"로그아웃에 실패하였습니다."),
	SIGNUP_ERROR(BAD_REQUEST,400,"회원가입에러입니다."),
	EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, 400,"만료된 토큰입니다."),
	JWT_PARSE_FAILED(BAD_REQUEST,404,"토큰 파싱이 잘못되었습니다."),

	KAKAO_USER_ERROR(BAD_REQUEST,404,"카카오 유저 정보를 가져오지 못하였습니다."),
	TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500, "토큰을 제대로 생성하지 못하였습니다."),

	NO_REGION(BAD_REQUEST,404,"존재하지 않는 지역입니다."),

	UPLOAD_FAIL(BAD_REQUEST,404,"이미지 업로드에 실패하였습니다."),

	USER_NOT_FOUND(BAD_REQUEST,404,"존재하지 않는 유저입니다."),

	COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 코스를 찾을 수 없습니다."),
	ALREADY_ENDED_COURSE(HttpStatus.BAD_REQUEST, 400, "이미 종료된 코스입니다."),
	USER_NOT_ON_COURSE(HttpStatus.BAD_REQUEST, 400, "코스를 진행중인 유저가 아닙니다."),
	COURSE_NOT_MATCH(HttpStatus.BAD_REQUEST, 400, "진행 중인 코스와 일치하지 않습니다."),
	ALREADY_IN_PROGRESS_COURSE(HttpStatus.BAD_REQUEST, 400, "이미 진행 중인 코스가 있습니다. 기존 코스를 먼저 종료해주세요."),

	CERTIFICATION_CENTER_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, "조건에 맞는 인증센터를 찾을 수 없습니다."),

	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "해당 장소를 찾을 수 없습니다."),
	TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 종류의 약관을 찾을 수 없습니다."),

	GOOGLE_USER_NOT_FOUND(NOT_FOUND,404,"구글 유저를 찾을 수 없습니다."),

	PENDING_USER(BAD_REQUEST,404,"PENDING 상태의 유저입니다. 회원가입을 완료해주세요"),

	HASH_MISMATCH(HttpStatus.BAD_REQUEST, 400, "인증 해시값이 일치하지 않습니다."),

	COURSE_NOT_COMPLETED(HttpStatus.BAD_REQUEST, 400, "아직 코스를 완료하지 않았습니다."),
	REWARD_ALREADY_GRANTED(HttpStatus.CONFLICT, 400, "이미 해당 코스에 대한 보상을 받았습니다."),


	;





	private final HttpStatus status;
	private final int code;
	private final String message;


	ErrorCode( HttpStatus status,int code, String message){
		this.code = code;
		this.status = status;
		this.message = message;
	}
}
