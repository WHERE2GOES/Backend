package backend.greatjourney.domain.community.controller.request;


import backend.greatjourney.domain.login.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
    private User user;
    private String name;

    private String description;

}
