package kz.flurent.model.response.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ServiceException extends RuntimeException {
    protected String message;
    protected ErrorCode errorCode;
    protected HttpStatus httpStatus;
}
