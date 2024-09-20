package backend.greatjourney.domain.community.controller;

import backend.greatjourney.domain.community.controller.request.PostRequestDTO;
import backend.greatjourney.domain.community.controller.response.PostResponseDTO;
import backend.greatjourney.domain.community.controller.response.SliceResponse;
import backend.greatjourney.domain.community.entity.Community_Comment;
import backend.greatjourney.domain.community.entity.Posting;
import backend.greatjourney.domain.community.service.CreatePostService;
import backend.greatjourney.global.exception.BaseResponse;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CreatePostService createPostService;

    //게시글 생성하는 api
    @PostMapping("/create")
    public BaseResponse createPost(@RequestBody PostRequestDTO postRequestDTO, @RequestHeader("Authorization") String token) {

        PostResponseDTO.postDetail postDetail = createPostService.makePost(postRequestDTO,token);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("게시글이 작성되었습니다.")
                .build();
    }
    //게시글 수정하는 api
    @PostMapping("/modify/{postId}")
    public BaseResponse modifyPost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable Long postId, @RequestHeader("Authorization") String token) {

        PostResponseDTO.postDetail postDetail = createPostService.modifyPost(postRequestDTO,token,postId);
        if (postDetail == null) {
            return BaseResponse.builder()
                    .code(401)
                    .isSuccess(false)
                    .message("해당 게시글의 주인이 아닙니다.")
                    .build();
        }
        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("게시글을 수정하였습니다.")
                .build();
    }

    //게시글을 삭제하는 api
    @PostMapping("/delete")
    public BaseResponse deletePost(@RequestParam Long postId, @RequestHeader("Authorization") String token) {
        String response = createPostService.deletePost(postId,token);

        if (response == null) {
            return BaseResponse.builder()
                    .code(401)
                    .isSuccess(false)
                    .message("해당 게시글의 주인이 아닙니다.")
                    .build();
        }else{
            return BaseResponse.builder()
                    .code(200)
                    .isSuccess(true)
                    .message("게시글을 삭제하였습니다.")
                    .build();
        }

    }

    //게시글 전체 리스트를 가져오는 api
    @GetMapping("posts")
    public BaseResponse getAllPosts() {

        List<Posting> posts = createPostService.getPostAll();

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .message("전체 게시글 리스트를 가져오기")
                .data(posts)
                .build();
    }

    //위치를 통해서 찾는 api
    @GetMapping("/location/{location}")
    public BaseResponse getPostByLocation(@RequestBody PostRequestDTO postRequestDTO,@PageableDefault(size = 10) Pageable pageable) {
        SliceResponse postDetailData;

        postDetailData = createPostService.getPostListWithLocation(postRequestDTO.getLocation(),pageable);

        //로그인이냐 비로그인이냐에 따라 결과가 달라야하지만 우리는 로그인과 비 로그인의 차이가 댓글 말고는 없다
        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetailData)
                .message("위치에 따른 데이터 조회")
                .build();
    }

    //게시글 상세하게 보는 api
    //게시글을 상세하게 보는 경우 view +1이 되게끔 구현
    @GetMapping("/details/{postId}")
    public BaseResponse getPostDetail(@RequestBody @PathVariable Long postId) {
        PostResponseDTO.postDetail postDetail = createPostService.getPostDetail(postId);

        //이때마다 조회수를 늘려줄 수 있게끔 작동하는 것이다.
        createPostService.updateView(postId);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("게시글 상세 정보 가져오기")
                .build();
    }

    //댓글 작성
    @PostMapping("/details/{postId}/comment")
    public BaseResponse createPostComment(@RequestBody Long postId ,@RequestBody String comment,@RequestHeader("Authorization") String token) {

        PostResponseDTO.postDetail postDetail = createPostService.creatComment(postId,comment,token);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("댓글 작성완료")
                .build();
    }
    //댓글 수정
    @PostMapping("/details/{postId}/comment/modify")
    public  BaseResponse modifyPostComment(@RequestBody @PathVariable Long postId ,@RequestBody Long commentId,@RequestBody String comment,@RequestHeader("Authorization") String token) {
        PostResponseDTO.postDetail postDetail = createPostService.modifyComment(postId,commentId,comment,token);


        if (postDetail == null) {
            return BaseResponse.builder()
                .code(401)
                .isSuccess(false)
                .message("해당 게시글의 주인이 아닙니다.")
                .build();
        }

        return BaseResponse.builder()
            .code(200)
            .isSuccess(true)
            .data(postDetail)
            .message("댓글 수정완료")
            .build();
    }

    //댓글 삭제
    @PostMapping("/comment/delete")
    public BaseResponse deletePostComment(@RequestBody Long commentId,@RequestHeader("Authorization") String token) {

        String responses = createPostService.deleteComment(commentId,token);

        if (responses == null) {
            return BaseResponse.builder()
                .code(401)
                .isSuccess(false)
                .message("해당 게시글의 주인이 아닙니다.")
                .build();
        }else{
            return BaseResponse.builder()
                    .code(200)
                    .isSuccess(true)
                    .message("댓글 삭제완료")
                    .build();
        }
    }

}
