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

    private User user;


    private String image_url;

    private String title;

    private String nickname;

    private String contents;

    private String location;

    //우선은 이렇게 만들고 별로면 하나의 클래스를 더 만들어서 채워넣는 식으로 하자
    public Posting toEntity(User user) {
        return Posting.builder()
                .user(user)
                .contents(contents)
                .image_url(image_url)
                .title(title)
                .nickname(nickname)
                .location(location)
                .build();

    }

//    public static class LocationReqeustDTO{
//
//        private User user;
//
//        private String location;
//
//
//
//
//
//    }


}
