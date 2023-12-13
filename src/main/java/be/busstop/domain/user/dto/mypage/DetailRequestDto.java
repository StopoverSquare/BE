package be.busstop.domain.user.dto.mypage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DetailRequestDto {
    @NotBlank(message = "성별은 필수 입력 값입니다.")
    String gender;

    @NotBlank(message = "나이는 필수 입력 값입니다.")
    String age;
}
