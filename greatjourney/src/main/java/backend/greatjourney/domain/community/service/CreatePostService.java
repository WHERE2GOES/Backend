package backend.greatjourney.domain.community.service;


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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePostService {

    private final PostingRepository postingRepository;
    private final UserRepository userRepository;
    private final Community_CommentRepository commentRepository;

    //게시글 생성
    public PostResponseDTO.postDetail makePost(PostRequestDTO postRequestDTO) {

        User user = userRepository.findById(postRequestDTO.getId()).orElseThrow();

        //DB에 저장하는 코드
        Posting post = postingRepository.save(postRequestDTO.toEntity(user));

        //결과를 따로 형식을 만들어서 리턴시켜주는 것이다.
        //성공할 때와 실패할 때에 대한 예외처리 필요함
        return PostResponseDTO.postDetail.of(post);
    }

    //게시글 상세 검색
    public PostResponseDTO.postDetail getPostDetail(Long postId) {
        Posting post = postingRepository.findById(postId).orElseThrow();

        return PostResponseDTO.postDetail.of(post);
    }

    //위치에 따른 게시물 출력
    //여기에 왜 userId가 필요한지를 모르겠네
    //pageable을 어떻게 구현해야하는지 모르겠음 .
    //이 부분에 대한 공부를 보충하는게 필요함
    //이때 baseResponse에 데이터를 넣어서 넘기는구나 ㅇㅋ
    public SliceResponse getPostListWithLocation(String location, Pageable pageable) {
        Slice<Posting> postList = postingRepository.findAllPostByLocation(location, pageable);
        Slice<PostResponseDTO.postDetail> postDtoList = postList.map(post -> PostResponseDTO.postDetail.of(post));
        return new SliceResponse<>(postDtoList);
    }


    //조회수를 증가시키는 함수
    @Transactional
    public void updateView(Long postId) {
        //findBy하게 되면 db에서 값을 가져온다는 얘기다.
        Posting post = postingRepository.findById(postId).orElseThrow();
        post.updateView(post.getView()+1);

    }


    //댓글을 작성하는 기능
    //댓글이 등록된 정보를 새로 가져다줘서 작성된 것을 바로 확인할 수 있게끔 설정
    //작성된 포스트를 리턴하는 식으로 제공하는 것
    public PostResponseDTO.postDetail creatComment(Long postId, String comment) {
        Posting post = postingRepository.findById(postId).orElseThrow();

        //user정보도 가져와야 한다.
        User user = post.getUser();

        Community_Comment communityComment = new Community_Comment(user,post,comment);

        //save 하기 전에 comment에 필요한 것을 다 만들어놓고 해야한다.
        //comment 저장
        Community_Comment communityResponse = commentRepository.save(communityComment);

        Posting post2 = postingRepository.findById(postId).orElseThrow();

        return PostResponseDTO.postDetail.of(post2);
    }




}
