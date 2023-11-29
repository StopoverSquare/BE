package be.busstop.domain.recommend.controller;

import be.busstop.domain.recommend.service.RecommendService;
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
@RequestMapping("/post/{postId}/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping
    public ResponseEntity<?> updateRecommend(@PathVariable Long postId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.ok(recommendService.updateRecommend(postId, userDetailsImpl.getUser()));
    }
}
