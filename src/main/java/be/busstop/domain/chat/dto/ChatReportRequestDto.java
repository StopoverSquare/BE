package be.busstop.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatReportRequestDto {

    @NotBlank(message = "신고내용을 입력하세요.")
    private ChatReportEnum report;

}
