package be.busstop.domain.user.dto.mypage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = "닉네임은 한글, 영문 대소문자, 숫자만 입력하여야 합니다.")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    String nickname;
    @NotBlank(message = "관심사는 필수 입력 값입니다.")
    String interest;
    @NotBlank(message = "성별은 필수 입력 값입니다.")
    String gender;
    @NotBlank(message = "나이는 필수 입력 값입니다.")
    String age;
}
