package be.busstop.domain.notification.dto;

import be.busstop.domain.notification.entity.Notification;
import be.busstop.domain.notification.util.AlarmType;
import be.busstop.domain.notification.util.Chrono;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NotificationResponseDto {

    private Long notificationId;

    private String message;

    private Boolean readStatus;

    private AlarmType alarmType;

    private String createdAt;

    private String senderUsername;

    private String senderNickname;

    private String senderProfileImageUrl;

    private String url;


    @Builder
    public NotificationResponseDto(Long id, String message, Boolean readStatus,
                                   AlarmType alarmType, String createdAt,
                                   String senderUsername, String senderNickname, String senderProfileImageUrl,
                                   String url) {
        this.notificationId = id;
        this.message = message;
        this.readStatus = readStatus;
        this.alarmType = alarmType;
        this.senderUsername = senderUsername;
        this.senderNickname = senderNickname;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.createdAt = createdAt;
        this.url = url;
    }

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.getMessage();
        this.readStatus = notification.getIsRead();
        this.alarmType = notification.getAlarmType();
        this.createdAt = Chrono.timesAgo(notification.getCreatedAt());
        this.senderNickname = notification.getSenderNickname();
        this.senderProfileImageUrl = notification.getSenderProfileImageUrl();
        this.url = notification.getUrl();
    }

    public static NotificationResponseDto create(Notification notification) {
        String createdAt = Chrono.timesAgo(notification.getCreatedAt());

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .alarmType(notification.getAlarmType())
                .readStatus(notification.getIsRead())
                .senderNickname(notification.getSenderNickname())
                .senderProfileImageUrl(notification.getSenderProfileImageUrl())
                .createdAt(createdAt)
                .url(notification.getUrl())
                .build();
    }
}
