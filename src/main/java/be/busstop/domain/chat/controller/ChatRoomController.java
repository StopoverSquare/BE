package be.busstop.domain.chat.controller;

import be.busstop.domain.chat.dto.ChatRoom;
import be.busstop.domain.chat.repository.RedisChatRepository;
import be.busstop.domain.chat.service.ChatService;
import be.busstop.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final RedisChatRepository redisChatRepository;
    private final ChatService chatService;

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        List<ChatRoom> chatRoom = redisChatRepository.findAllRoom();
        chatRoom.stream().forEach(room -> room.setUserCount(redisChatRepository.getUserCount(room.getRoomId())));
        return chatRoom;
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails, Long subLimit) {
        return chatService.createRoom(userDetails.getUser().getNickname(), subLimit);
    }
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return redisChatRepository.findRoomById(roomId);
    }

    @GetMapping("/rooms/user")
    public ResponseEntity<List<ChatRoom>> userRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.getUserRooms(userDetails.getUser());
    }

    @PostMapping("/room/post/{roomId}")
    @ResponseBody
    public ChatRoom addParticipantToChatRoomByPost(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.addParticipantToChatRoomByPost(roomId,userDetails.getUser());
    }

    @DeleteMapping("/room/post/{roomId}")
    public ResponseEntity<String> leaveRoomByPost(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.leaveRoomByPost(roomId,userDetails.getUser());
    }

    @GetMapping("/room")
    public String rooms() {
        return "chat/room";
    }


    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }
}