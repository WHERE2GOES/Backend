package backend.greatjourney.domain.community.controller;

import backend.greatjourney.domain.community.controller.request.*;
import backend.greatjourney.domain.community.controller.response.PostResponseDTO;
import backend.greatjourney.domain.community.controller.response.SliceResponse;
import backend.greatjourney.domain.community.entity.Community_Comment;
import backend.greatjourney.domain.community.entity.Posting;
import backend.greatjourney.domain.community.service.CreatePostService;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.repository.UserRepository;
import backend.greatjourney.domain.login.service.FileUploadService;
import backend.greatjourney.global.exception.BaseResponse;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CreatePostService createPostService;
    private final FileUploadService fileUploadService;

    //게시글 생성하는 api
    @PostMapping("/create")
    public BaseResponse createPost( @RequestBody PostRequestDTO postRequestDTO, @RequestHeader("Authorization") String token)  throws IOException {

        PostResponseDTO.postDetail postDetail = createPostService.makePost(postRequestDTO,token);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("게시글이 작성되었습니다.")
                .build();
    }

    //게시글 수정하는 api
    @PostMapping("/modify")
    public BaseResponse modifyPost(@RequestBody PostModifyRequest postModifyRequest, @RequestHeader("Authorization") String token) throws IOException {

        PostResponseDTO.postDetail postDetail = createPostService.modifyPost(postModifyRequest,token);

        if (postDetail == null) {
            return BaseResponse.builder()
                    .code(401)
                    .isSuccess(false)
                    .data(null)
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

    //게시글 생성과 수정에서 사용하기 위해서 파일을 업로드하는 api
    @PostMapping("/image/upload")
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
        String url = fileUploadService.uploadFile(file);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(url)
                .message("사진 업로드에 성공하였습니다.")
                .build();
    }


    //게시글을 삭제하는 api
    @PostMapping("/delete")
    public BaseResponse deletePost(@RequestBody PostDeleteRequest postDeleteRequest, @RequestHeader("Authorization") String token) {
        String response = createPostService.deletePost(postDeleteRequest.getPostId(),token);

        if (response == null) {
            return BaseResponse.builder()
                    .code(401)
                    .isSuccess(false)
                    .data(null)
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
    public BaseResponse getAllPosts(@RequestHeader("Authorization")String token) throws IOException {

        List<Posting> posts = createPostService.getPostAll(token);

        if(posts == null){
            return BaseResponse.builder()
                    .code(400)
                    .isSuccess(false)
                    .message("회원이 아닙니다.")
                    .data(null)
                    .build();
        }
        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .message("전체 게시글 리스트를 가져오기")
                .data(posts)
                .build();
    }

    //위치를 통해서 찾는 api
    @GetMapping("/location")
    public BaseResponse getPostByLocation(@RequestParam String location,@RequestParam int page, @RequestParam int size ,@RequestHeader("Authorization")String token) {
        SliceResponse<PostResponseDTO.postDetail> postDetailData;

        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));

        postDetailData = createPostService.getPostListWithLocation(location,pageable);

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
    @GetMapping("/details")
    public BaseResponse getPostDetail(@RequestParam Long postId,@RequestHeader("Authorization")String token) throws IOException {

       if( createPostService.checkToken(token) == null){
           return BaseResponse.builder()
                   .code(400)
                   .isSuccess(false)
                   .data(null)
                   .message("권한이 없는 사용자입니다.")
                   .build();
       }

        PostResponseDTO.postDetail postDetail = createPostService.getPostDetail(postId,token);


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
    @PostMapping("/comment")
    public BaseResponse createPostComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String token) {

        PostResponseDTO.postDetail postDetail = createPostService.creatComment(commentRequest.getPostId(),commentRequest.getComment(),token);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("댓글 작성완료")
                .build();
    }
    //댓글 수정
    @PostMapping("/comment/modify")
    public  BaseResponse modifyPostComment(@RequestBody CommentModifyRequest commentModifyRequest, @RequestHeader("Authorization") String token) {

        PostResponseDTO.postDetail postDetail = createPostService.modifyComment(commentModifyRequest.getPostId(),commentModifyRequest.getCommentId(),commentModifyRequest.getComment(),token);

        if (postDetail == null) {
            return BaseResponse.builder()
                .code(401)
                .isSuccess(false)
                .data(null)
                .message("해당 댓글의 주인이 아닙니다.")
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
    public BaseResponse deletePostComment(@RequestBody CommentDeleteRequest commentDeleteRequest,@RequestHeader("Authorization") String token) {

        String responses = createPostService.deleteComment(commentDeleteRequest.getCommentId(),token);

        if (responses == null) {
            return BaseResponse.builder()
                .code(401)
                .isSuccess(false)
                .data(null)
                .message("해당 게시글의 주인이 아닙니다.")
                .build();
        }else{
            return BaseResponse.builder()
                    .code(200)
                    .isSuccess(true)
                    .data(responses)
                    .message("댓글 삭제완료")
                    .build();
        }
    }

}
