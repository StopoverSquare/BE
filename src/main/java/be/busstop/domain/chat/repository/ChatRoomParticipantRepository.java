package be.busstop.domain.chat.repository;

import be.busstop.domain.chat.dto.ChatRoom;
import be.busstop.domain.chat.entity.ChatRoomEntity;
import be.busstop.domain.chat.entity.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
    Long countAllByChatRoom(ChatRoomEntity chatRoom);
}
