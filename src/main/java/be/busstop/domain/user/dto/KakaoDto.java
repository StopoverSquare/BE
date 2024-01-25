package be.busstop.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoDto {
    private Long id;
    private String userCode;
    private String profileImageUrl;

    public KakaoDto(Long id, String userCode, String profileImageUrl) {
        this.id = id;
        this.userCode = userCode;
        this.profileImageUrl = profileImageUrl;
    }
}