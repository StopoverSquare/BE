package be.busstop.domain.chat.controller;


import be.busstop.domain.chat.config.handler.StompHandler;
import be.busstop.domain.chat.dto.ChatMessage;
import be.busstop.domain.chat.service.ChatService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final StompHandler stompHandler;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Valid @Header("Authorization") String token) {
        chatService.sendChatMessage(chatService.getUserDetail(message,token));
    }

    @PostMapping("/chat/image")
    @ResponseBody
    public ApiResponse<?> uploadImage(@RequestPart(value = "file", required = false) MultipartFile file,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.success(chatService.uploadImage(file, userDetails.getUser()));
    }

    @GetMapping("/chat/{roomId}")
    @ResponseBody
    public Slice<ChatMessage> getChatMessages(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable String roomId,
                                              Pageable page) {
        return chatService.getChatMessages(userDetails.getUser(),roomId, page);
    }

    @PostMapping("/chat/disconnect/{roomId}/{targetId}")
    @ResponseBody
    public ApiResponse<?> disconnectUser(@PathVariable String roomId,
                                         @PathVariable Long targetId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.success(stompHandler.disconnectUser(userDetails.getUser(),targetId,roomId));
    }
}
