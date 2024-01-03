package be.busstop.domain.user.dto;

import be.busstop.domain.user.entity.UserReportEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportDto {
    @NotBlank(message = "신고분류를 입력하세요.")
    private UserReportEnum report;
    @Pattern(regexp = "^[\\s\\S]{1,500}$", message = "내용은 1자에서 500자까지만 허용되며 줄바꿈도 허용됩니다.")
    private String reportDetail;
}
