package be.busstop.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoDto {
    private Long id;
    private String username;
    private String profileImageUrl;

    public KakaoDto(Long id, String username, String profileImageUrl) {
        this.id = id;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }
}