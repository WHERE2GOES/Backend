package backend.greatjourney.domain.community.repository;

import backend.greatjourney.domain.community.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting,Long> {

}
