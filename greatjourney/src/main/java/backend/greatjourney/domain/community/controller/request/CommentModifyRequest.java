package backend.greatjourney.domain.community.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentModifyRequest {
    private Long postId;
    private Long commentId;
    private String comment;
}
