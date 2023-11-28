package be.busstop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank(message = "닉네임 공백 불가")
    private String nickname;

    @NotBlank(message = "password 공백 불가")
    private String password;
}
