package be.busstop.domain.chat.entity;

import be.busstop.domain.chat.dto.ChatMessage;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity extends Timestamped implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private ChatMessage.MessageType type;

    private String roomId;

    private Long senderId;

    private String sender;

    private String message;

    private String imageUrl;

    private Long userCount;

    private String profileImageUrl;

    private long reportCount;



    public ChatMessageEntity(ChatMessage chatMessage) {
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.sender = chatMessage.getSender();
        this.senderId = chatMessage.getSenderId();
        this.message = chatMessage.getMessage();
        this.imageUrl = chatMessage.getImageUrl();
        this.userCount = chatMessage.getUserCount();
        this.profileImageUrl = chatMessage.getProfileImageUrl();
        this.createdAt = chatMessage.getCreatedAt();
    }

    public ChatMessage toDto() {
        return ChatMessage.builder()
                .type(type)
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .imageUrl(imageUrl)
                .userCount(userCount)
                .profileImageUrl(profileImageUrl)
                .createdAt(createdAt)
                .build();
    }
    public void increaseReportCount(){
        this.reportCount ++;
    }
}
