package be.busstop.domain.chat.repository;

import be.busstop.domain.chat.dto.ChatMessage;
import be.busstop.domain.chat.dto.ChatMessageSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface ChatMessageRepositoryCustom {

    Slice<ChatMessage> searchChatMessages(ChatMessageSearchCondition condition, Pageable pageable);
}
