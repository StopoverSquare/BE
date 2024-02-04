package be.busstop.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminResponseDto {
    private String nickname;
    private String profileImg;
    private String age;
    private String gender;
    private boolean isSuper;
}
