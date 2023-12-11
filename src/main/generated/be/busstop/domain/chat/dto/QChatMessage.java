package be.busstop.domain.chat.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * be.busstop.domain.chat.dto.QChatMessage is a Querydsl Projection type for ChatMessage
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QChatMessage extends ConstructorExpression<ChatMessage> {

    private static final long serialVersionUID = 2121840285L;

    public QChatMessage(com.querydsl.core.types.Expression<? extends be.busstop.domain.chat.entity.ChatMessageEntity> chatMessageEntity) {
        super(ChatMessage.class, new Class<?>[]{be.busstop.domain.chat.entity.ChatMessageEntity.class}, chatMessageEntity);
    }

}

