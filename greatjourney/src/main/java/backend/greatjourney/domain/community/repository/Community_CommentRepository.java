package backend.greatjourney.domain.community.repository;

import backend.greatjourney.domain.community.entity.Community_Comment;
import backend.greatjourney.domain.community.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Community_CommentRepository extends JpaRepository<Community_Comment, Long> {
    //Community_comment를 가져오기 위한 메소드이다.
    List<Community_Comment> findCommunity_CommentByPosting(Posting posting);

}
