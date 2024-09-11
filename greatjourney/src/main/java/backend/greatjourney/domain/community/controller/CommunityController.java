package backend.greatjourney.domain.community.controller;

import backend.greatjourney.domain.community.controller.request.PostRequestDTO;
import backend.greatjourney.domain.community.controller.response.PostResponseDTO;
import backend.greatjourney.domain.community.controller.response.SliceResponse;
import backend.greatjourney.domain.community.service.CreatePostService;
import backend.greatjourney.global.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CreatePostService createPostService;

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

    //게시글 생성하는 api
    @PostMapping("/create")
    public BaseResponse createPost(@RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO.postDetail postDetail = createPostService.makePost(postRequestDTO);

        return BaseResponse.builder()
                .code(200)
                .isSuccess(true)
                .data(postDetail)
                .message("게시글이 작성되었습니다.")
                .build();
    }

    //게시글 상세하게 보는 api
    //게시글을 상세하게 보는 경우 view +1이 되게끔 구현
    @GetMapping("/details/{postId}")
    public BaseResponse getPostDetail(@RequestBody Long postId) {
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









}
