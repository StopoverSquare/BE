package be.busstop.domain.chat.repository;

import be.busstop.domain.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, ChatMessageRepositoryCustom {

}
