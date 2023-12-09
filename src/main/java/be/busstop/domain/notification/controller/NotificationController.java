package be.busstop.domain.notification.controller;

import be.busstop.domain.notification.dto.MessageStatusResponseDto;
import be.busstop.domain.notification.dto.NotificationResponseDto;
import be.busstop.domain.notification.service.NotificationService;
import be.busstop.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor //생성자를 자동으로 생성
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)///subscribe 엔드포인트로 들어오는 요청을 처리. produces 속성은 해당 메서드가 반환하는 데이터 형식을 지정
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter subscribe( @AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId)  {
        return notificationService.subscribe(userDetails, lastEventId);
    }

    @GetMapping("/all")
    public MessageStatusResponseDto<List<NotificationResponseDto>> getAllNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return MessageStatusResponseDto.success(HttpStatus.OK, notificationService.getAllNotifications(userDetails.getUser().getId()));
    }

    @GetMapping("/unread")
    public MessageStatusResponseDto<List<NotificationResponseDto>> getUnreadNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return MessageStatusResponseDto.success(HttpStatus.OK, notificationService.getUnreadNotification(userDetails.getUser()));
    }

    @DeleteMapping("/{notificationId}")
    public MessageStatusResponseDto<NotificationResponseDto> deleteNotification(@PathVariable Long notificationId,
                                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        notificationService.deleteNotification(notificationId,userDetails.getUser());
        return MessageStatusResponseDto.success(HttpStatus.OK,null);
    }

    @DeleteMapping("/allDelete")
    public MessageStatusResponseDto<List<NotificationResponseDto>> allDeleteNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.allDeleteNotification(userDetails.getUser());
        return MessageStatusResponseDto.success(HttpStatus.OK, null);
    }

    @PostMapping("/read/{notificationId}")
    public MessageStatusResponseDto<NotificationResponseDto> readNotification(@PathVariable Long notificationId,
                                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.readNotification(notificationId, userDetails.getUser());
        return MessageStatusResponseDto.success(HttpStatus.OK, null);
    }
}