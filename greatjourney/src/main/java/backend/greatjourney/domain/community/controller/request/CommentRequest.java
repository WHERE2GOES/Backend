package backend.greatjourney.domain.community.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long postId;
    private String comment;
}
