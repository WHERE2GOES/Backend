package backend.greatjourney.domain.community.service;


import backend.greatjourney.domain.community.controller.request.PostModifyRequest;
import backend.greatjourney.domain.community.controller.request.PostRequestDTO;
import backend.greatjourney.domain.community.controller.response.PostResponseDTO;
import backend.greatjourney.domain.community.controller.response.SliceResponse;
import backend.greatjourney.domain.community.entity.Community_Comment;
import backend.greatjourney.domain.community.entity.Posting;
import backend.greatjourney.domain.community.repository.Chat_MessageRepository;
import backend.greatjourney.domain.community.repository.Chat_RoomRepository;
import backend.greatjourney.domain.community.repository.Community_CommentRepository;
import backend.greatjourney.domain.community.repository.PostingRepository;
import backend.greatjourney.domain.login.domain.User;
import backend.greatjourney.domain.login.repository.UserRepository;
import backend.greatjourney.global.hashing.TokenHashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePostService {

    private final PostingRepository postingRepository;
    private final UserRepository userRepository;
    private final Community_CommentRepository commentRepository;
    private final TokenHashing tokenHashing;
    private final Community_CommentRepository communityCommentRepository;
    //게시글 생성
    public PostResponseDTO.postDetail makePost(PostRequestDTO postRequestDTO,String token) {

        //토큰 값은 헤더를 통해서 전달하기 때문에 그걸 갖고 와서 열여주면 됨
        Long userId = tokenHashing.getUserIdFromRequest(token);

        //문자열로 나온 userId를 변경해서 넘김
        User user = userRepository.findById(userId).orElseThrow();

        //DB에 저장하는 코드
        Posting post = postingRepository.save(postRequestDTO.toEntity(user));

        //결과를 따로 형식을 만들어서 리턴시켜주는 것이다.
        //성공할 때와 실패할 때에 대한 예외처리 필요함
        return PostResponseDTO.postDetail.of(post);
    }

    //게시글 수정
    public PostResponseDTO.postDetail modifyPost(PostModifyRequest postModifyRequest, String token) {

        //유저에 대한 정보를 가져옴
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();

        //게시글을 가져옴

        Posting posting = postingRepository.findById(postModifyRequest.getPostId()).orElseThrow();

        //같은 유저의 경우
        if(posting.getUser().equals(user)){
            posting.setContents(postModifyRequest.getContents());
            posting.setTitle(postModifyRequest.getTitle());
            posting.setImage_url(postModifyRequest.getImage_url());
            postingRepository.save(posting);

            return PostResponseDTO.postDetail.of(posting);
            }else {
            return null;
        }

    }


    //모든 게시글 불러오기
    public List<Posting> getPostAll(String token) {
        Long userId = tokenHashing.getUserIdFromRequest(token);
        if(!userRepository.existsById(userId)){
            return null;
        }
        return postingRepository.findAll();
    }

    //게시글 상세 검색
    public PostResponseDTO.postDetail getPostDetail(Long postId, String token) {
        Posting post = postingRepository.findById(postId).orElseThrow();


        //유저에 대한 정보를 가져옴
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();

        return PostResponseDTO.postDetail.ofDetail(post,user);
    }


    //위치에 따른 게시물 출력
    //여기에 왜 userId가 필요한지를 모르겠네
    //pageable을 어떻게 구현해야하는지 모르겠음 .
    //이 부분에 대한 공부를 보충하는게 필요함
    //이때 baseResponse에 데이터를 넣어서 넘기는구나 ㅇㅋ
    public SliceResponse<PostResponseDTO.postDetail> getPostListWithLocation(String location, Pageable pageable) {
        Slice<Posting> postList = postingRepository.findAllPostByLocation(location, pageable);
        Slice<PostResponseDTO.postDetail> postDtoList = postList.map(post -> PostResponseDTO.postDetail.of(post));
        return new SliceResponse<>(postDtoList);
    }


    //게시글 삭제하는 함수
    public String deletePost(Long postId, String token) {

        //user값을 구하는 것
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();

        Posting post = postingRepository.findById(postId).orElseThrow();
        if(post.getUser().equals(user)){
            postingRepository.delete(post);
            return "success";
        }else{
            return null;
        }
    }

    //조회수를 증가시키는 함수
    @Transactional
    public void updateView(Long postId) {
        //findBy하게 되면 db에서 값을 가져온다는 얘기다.
        Posting post = postingRepository.findById(postId).orElseThrow();
        //view가 왜 null로 들어갔을까?

        if(post.getView()!=null){
            post.updateView(post.getView()+1);
        }
    }

    //댓글을 작성하는 기능
    //댓글이 등록된 정보를 새로 가져다줘서 작성된 것을 바로 확인할 수 있게끔 설정
    //작성된 포스트를 리턴하는 식으로 제공하는 것
    public PostResponseDTO.postDetail creatComment(Long postId, String comment, String token) {

        Posting post = postingRepository.findById(postId).orElseThrow();

        //user정보도 가져와야 한다.
        //현재 댓글을 작성하는 유저에 대한 정보를 가져와야 함.
//        User postHostUser = post.getUser();

        //여기서 user는 댓글의 소유자가 되어야 함.
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();

        Community_Comment communityComment = new Community_Comment(user,post,comment);

        //save 하기 전에 comment에 필요한 것을 다 만들어놓고 해야한다.
        //comment 저장
        Community_Comment communityResponse = commentRepository.save(communityComment);

        Posting post2 = postingRepository.findById(postId).orElseThrow();

        //이게 post가 아니라 답글 리스트가 되어야할 것 같은데
        return PostResponseDTO.postDetail.of(post2);
    }


    //댓글 수정하는 함수
    //기존의 댓글의 id로 찾는 방법도 있고 그냥 post에서 찾는 방법도 있다.
    public PostResponseDTO.postDetail modifyComment(Long postId,Long commentId, String comment, String token) {

        //댓글을 작성한 사람만 고칠 수 있게끔 유효성 검사 추가하기

        Posting post = postingRepository.findById(postId).orElseThrow();

        //userId를 통해서 유저정보를 가져온다.
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();


        //community comment를 구해서 그 안의 내용을 수정
        Community_Comment communityComment = communityCommentRepository.findById(commentId).orElseThrow();

        if (communityComment.getUser().equals(user)) {
            if(communityComment.getId().equals(commentId)){
                //comment id가 동일할 경우에만 그렇게 한다.
                communityComment.setContent(comment);

                communityCommentRepository.save(communityComment);

                return PostResponseDTO.postDetail.of(post);
            }else{
                return null;
            }

        } else {
            return null;
        }

    }
    //comment를 삭제하는 함수
    public String deleteComment(Long commentId, String token) {

        //user정보를 토큰에서 가져오는 것이다.
        Long userId = tokenHashing.getUserIdFromRequest(token);
        User user = userRepository.findById(userId).orElseThrow();

        Community_Comment communityComment = commentRepository.findById(commentId).orElseThrow();

        if(communityComment.getUser().equals(user)){
            commentRepository.delete(communityComment);
            return "success";
        }else{
            return null;
        }
    }

    public String checkToken(String token) {
        Long userId = tokenHashing.getUserIdFromRequest(token);
        if(userRepository.existsById(userId)){
            return "success";
        }else {
            return null;
        }
    }
}
