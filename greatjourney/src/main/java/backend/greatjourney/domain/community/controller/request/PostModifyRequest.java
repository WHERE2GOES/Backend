package backend.greatjourney.domain.community.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostModifyRequest {
    private Long postId;

    private String title;
    private String contents;
    private String location;
    private String image_url;
}
