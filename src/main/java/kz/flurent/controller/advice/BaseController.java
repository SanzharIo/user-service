package kz.flurent.controller.advice;

import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ErrorResponse;
import kz.flurent.model.response.errors.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<?> buildResponse(Object data, HttpStatus httpStatus) {
        return new ResponseEntity<>(data, httpStatus);
    }

    protected ResponseEntity<?> buildResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }

    protected ErrorResponse buildErrorResponse(ServiceException serviceException) {
        return ErrorResponse.builder()
                .code(serviceException.getErrorCode())
                .message(serviceException.getMessage())
                .build();
    }
    protected ErrorResponse buildErrorResponse(Exception e, ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode)
                .message(e.getMessage())
                .build();
    }
    protected ResponseEntity<?> buildSuccessResponse(Object data){
        return new ResponseEntity<>(data , HttpStatus.OK);
    }
}
