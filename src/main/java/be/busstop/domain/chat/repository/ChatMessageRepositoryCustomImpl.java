package be.busstop.domain.chat.repository;

import be.busstop.domain.chat.dto.ChatMessage;
import be.busstop.domain.chat.dto.ChatMessageSearchCondition;
import be.busstop.domain.chat.dto.QChatMessage;
import be.busstop.domain.chat.entity.QChatMessageEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;


import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Slice<ChatMessage> searchChatMessages(ChatMessageSearchCondition condition, Pageable pageable) {
        int customPageSize = 50;
        pageable = PageRequest.of(pageable.getPageNumber(), customPageSize, pageable.getSort());
        List<ChatMessage> result = query
                .select(new QChatMessage(QChatMessageEntity.chatMessageEntity))
                .from(QChatMessageEntity.chatMessageEntity)
                .where(
                        roomIdEq(condition.getRoomId()))
                .orderBy(QChatMessageEntity.chatMessageEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkEndPage(pageable, result);
    }

    private BooleanExpression roomIdEq(String roomIdCond) {
        return hasText(roomIdCond) ? QChatMessageEntity.chatMessageEntity.roomId.eq(roomIdCond) : null;
    }



    private static SliceImpl<ChatMessage> checkEndPage(Pageable pageable, List<ChatMessage> content) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
