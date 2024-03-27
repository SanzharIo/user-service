package kz.flurent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
    private boolean active;
    private boolean block;
    private String role;
}
