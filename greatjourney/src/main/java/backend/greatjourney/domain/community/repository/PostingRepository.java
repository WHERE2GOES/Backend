package backend.greatjourney.domain.community.repository;

import backend.greatjourney.domain.community.entity.Posting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {

    Slice<Posting> findAllPostByLocation(@Param("location") String genre, Pageable pageable);
}
