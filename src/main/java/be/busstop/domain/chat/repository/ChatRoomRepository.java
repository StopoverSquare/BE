package be.busstop.domain.chat.repository;

import be.busstop.domain.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

    List<ChatRoomEntity> findByChatRoomParticipants_UserId(Long userId);

    ChatRoomEntity findByRoomId(String roomId);
}