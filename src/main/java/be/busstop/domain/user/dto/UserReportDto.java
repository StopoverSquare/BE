package be.busstop.domain.user.dto;

import be.busstop.domain.user.entity.UserReportEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserReportDto {
    @NotBlank(message = "신고내용을 입력하세요.")
    private UserReportEnum report;
}
