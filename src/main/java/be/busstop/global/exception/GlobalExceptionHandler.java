package be.busstop.global.exception;

import be.busstop.global.responseDto.ApiResponse;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static be.busstop.global.utils.ResponseUtils.customError;
import static be.busstop.global.utils.ResponseUtils.error;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgsException(IllegalArgumentException ie) {
        return error(ie.getMessage(), BAD_REQUEST.value());
    }


    @ExceptionHandler(AmazonS3Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleAmazonS3Exception(AmazonS3Exception ae) {
        return error(ae.getMessage(), BAD_REQUEST.value());
    }


    @ExceptionHandler(InvalidConditionException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleUserException(InvalidConditionException e) {
        return customError(e.errorCodeEnum);
    }


    @ExceptionHandler(UploadException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleUploadsException(UploadException e) {
        return customError(e.errorCodeEnum);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResponse<?> handleValidationErrors(MethodArgumentNotValidException me) {
        BindingResult bindingResult = me.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder validMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            validMessage.append(fieldError.getDefaultMessage());
            validMessage.append(" ");
        }
        return error(String.valueOf(validMessage), BAD_REQUEST.value());
    }

}
