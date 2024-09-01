package backend.greatjourney.domain.community.repository;

import backend.greatjourney.domain.community.entity.Chat_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Chat_RoomRepository extends JpaRepository<Chat_Room, Long> {
    //JpaR

}
