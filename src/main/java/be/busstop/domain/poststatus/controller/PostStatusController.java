package be.busstop.domain.poststatus.controller;


import be.busstop.domain.poststatus.service.PostStatusService;
import be.busstop.domain.user.entity.User;
import be.busstop.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/post/{postId}")
@RequiredArgsConstructor
public class PostStatusController {

    private final PostStatusService postStatusService;

    @PostMapping
    public ResponseEntity<?> updatePostStatus(@PathVariable Long postId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.ok(postStatusService.updatePostStatus(postId, user));
    }
}
