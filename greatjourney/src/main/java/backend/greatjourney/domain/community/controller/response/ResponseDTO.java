package backend.greatjourney.domain.community.controller.response;

import backend.greatjourney.global.exception.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor(staticName = "set")
public class ResponseDTO {

//        private BaseResponse status;
        private String message; //이거 메세지를 set해주고 나서


        public static <T> BaseResponse<T> setSuccess(String message){
            return BaseResponse.<T>builder()
                    .code(2000)
                    .isSuccess(true)
                    .message("채팅을 보냈습니다."+message)
                    .build();
    }

        public static <T> BaseResponse<T> setBadRequest(String message){
            return BaseResponse.<T>builder()
                    .code(2001)
                    .isSuccess(false)
                    .message("메세지 전송에 오류가 발생했습니다.")
                    .build();
        }

}
