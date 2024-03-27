package kz.flurent.controller.error;

import kz.flurent.controller.advice.BaseController;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
@RestController
public class ErrorController extends BaseController {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<?> errors(HttpClientErrorException.NotFound e) {
        return buildResponse(buildErrorResponse(e, ErrorCode.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<?> errors(HttpClientErrorException.BadRequest e) {
        return buildResponse(buildErrorResponse(e, ErrorCode.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> errors(ServiceException e) {
        return buildResponse(buildErrorResponse(e), e.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> errors(RuntimeException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return buildResponse(buildErrorResponse(e, ErrorCode.INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}