package be.busstop.domain.notification.entity;

import be.busstop.domain.notification.util.AlarmType;
import be.busstop.domain.user.entity.User;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity(name = "notification")
@EqualsAndHashCode(of = "id", callSuper = true)
@Getter //각 래퍼 클래스에 대한 추출 메소드이다.
@NoArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //누구 : ~에 대한 알림이 도착했습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //한 번 알림을 읽으면 읽음 표시되어 더이상 우리에게 반짝이지 않는다
    private User receiver;
    @Column
    private String senderNickname;
    @Column
    private String senderProfileImageUrl;
    @Column
    private Boolean isRead = false; //  읽었는지에 대한 여부를 나타낸다

    @Column(nullable = false)
    private String message;  //알림의 내용. 비어있지 않아야하며 50자 이내여야한다.

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false)
    private AlarmType alarmType; // 알림 종류에 관한 것이다.

    @Column
    private String url;

    @Builder
    public Notification(User receiver, Boolean isRead,
                        String message, AlarmType alarmType,
                        String senderNickname,
                        String senderProfileImageUrl, String url) {
        this.receiver = receiver;
        this.isRead = false;
        this.message = message;
        this.alarmType = alarmType;
        this.senderNickname = senderNickname;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.url = url;
    }

    public void read(){
        this.isRead = true;
    }
}
