package be.busstop.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BlockedPostResponseDto {
    private Long postId;
    private String authorImg;
    private String authorNickname;
    private String authorAge;
    private String authorGender;
    private String postTitle;
    private LocalDate blockedDate;
}
