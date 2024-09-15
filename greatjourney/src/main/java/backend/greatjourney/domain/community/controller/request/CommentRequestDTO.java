package backend.greatjourney.domain.community.controller.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {


    private String postId;
    private String commentId;
    private String comment;




}
