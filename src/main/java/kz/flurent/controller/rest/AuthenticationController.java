package kz.flurent.controller.rest;

import kz.flurent.controller.advice.BaseController;
import kz.flurent.model.entity.User;
import kz.flurent.model.request.AuthRequest;
import kz.flurent.model.request.AuthenticationRequest;
import kz.flurent.model.response.UserResponse;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import kz.flurent.config.JwtService;
import kz.flurent.service.auth.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@Tag(name = "Authentication controller", description = "Контроллер авторизации и входа в систему")
@AllArgsConstructor
public class AuthenticationController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private JwtService jwtService;

    @PostMapping("/signup")
//    @Operation(summary = "Зарегистрировать пользователя")
    public ResponseEntity<UserResponse> register(@RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity.ok(authenticationService.createUser(authRequest));
    }

    @PostMapping("/login")
//    @Operation(summary = "Войти в систему")
    public ResponseEntity<Void> authenticate(@RequestBody AuthRequest authRequest) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            String token = jwtService.createToken((User) authentication.getPrincipal());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, token);

            return new ResponseEntity<>(headers, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new ServiceException("Invalid username or password", ErrorCode.AUTH_ERROR, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.AUTH_ERROR, HttpStatus.BAD_REQUEST);
        }
    }
}