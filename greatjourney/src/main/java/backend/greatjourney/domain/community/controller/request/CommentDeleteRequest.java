package backend.greatjourney.domain.community.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeleteRequest {
    private Long commentId;
}
