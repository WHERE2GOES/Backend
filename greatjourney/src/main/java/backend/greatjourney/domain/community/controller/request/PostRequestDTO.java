package backend.greatjourney.domain.community.controller.request;




import backend.greatjourney.domain.community.entity.Posting;
import backend.greatjourney.domain.login.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    //이제 user부분을 그냥 토큰을 받아서 할 수 있게끔 해야한다.
    //거기에 이제 토큰 값을 통해서 유저정보를 여기에 넣어줄 수 있게끔 할 수 있어야한다.
//    private String token;
//    private User user;



    private String title;

//    private String nickname;

    private String contents;

    private String location;
    private String image_url;

    //우선은 이렇게 만들고 별로면 하나의 클래스를 더 만들어서 채워넣는 식으로 하자
    //이게 캡슐화때문에 여기에 함수를 작성하는 것이다.
    public Posting toEntity(User user) {
        return Posting.builder()
                .user(user)
                .contents(contents)
                .image_url(image_url)
                .title(title)
                .location(location)
                .view(1L)
                .build();
    }

}
