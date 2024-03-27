package kz.flurent.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestoreRequest {
    private String destination;
    private String password;
    private String otp;
}
