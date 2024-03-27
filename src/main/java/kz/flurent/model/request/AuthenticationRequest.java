package kz.flurent.model.request;

import lombok.Data;
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;
}
