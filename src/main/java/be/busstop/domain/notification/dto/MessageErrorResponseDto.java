package be.busstop.domain.notification.dto;

import be.busstop.global.stringCode.ErrorCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MessageErrorResponseDto {
    ErrorCodeEnum errorType;

    String errorMessage;

    public void MessageErrorResponseDTO(ErrorCodeEnum errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }
}
