package be.busstop.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;

    public KakaoDto(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}