package be.busstop.global.utils;


import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.responseDto.ErrorResponse;
import be.busstop.global.stringCode.ErrorCodeEnum;
import be.busstop.global.stringCode.SuccessCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> okWithMessage(SuccessCodeEnum successCodeEnum) {
        return new ApiResponse<>(true, successCodeEnum.getMessage(), null);
    }
    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }

    public static ApiResponse<?> customError(ErrorCodeEnum errorCodeEnum) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCodeEnum));
    }

}
