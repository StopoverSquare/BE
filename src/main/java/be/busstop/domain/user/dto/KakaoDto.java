package be.busstop.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoDto {
    private Long id;
    private String nickname;
    private String age;
    private String gender;
    private String profileImageUrl;

    public KakaoDto(Long id, String nickname, String age, String gender, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }
}