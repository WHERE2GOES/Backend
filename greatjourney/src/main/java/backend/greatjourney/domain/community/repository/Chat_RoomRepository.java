package backend.greatjourney.domain.community.repository;

import backend.greatjourney.domain.community.entity.Chat_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Chat_RoomRepository extends JpaRepository<Chat_Room, Long> {
    //JpaR
    Optional<Chat_Room> findByRoomId(String roomId);

    Optional<Chat_Room> findByHostAndGuest(String host, String guest);

    void deleteByRoomId(String roomId);

}
