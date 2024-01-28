package be.busstop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResponseDto {
    private String nickname;
    private String profileImg;
    private String age;
    private String gender;
}
