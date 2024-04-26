package kz.flurent.controller.rest;

import kz.flurent.model.request.RestoreRequest;
import kz.flurent.model.response.OtpResponse;
import kz.flurent.model.response.UserResponse;
import kz.flurent.service.otp.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@AllArgsConstructor
public class OtpController {

    private OtpService otpService;

    @GetMapping("/check")
    public ResponseEntity<OtpResponse> checkOptCode(@RequestParam String destination, @RequestParam String code) {
        return new ResponseEntity<>(otpService.checkOtpRequest(destination,code), HttpStatus.OK);
    }

    @PostMapping("/restore")
//    @Operation(summary = "Восстановить аккаунт")
    public ResponseEntity<UserResponse> restore(@RequestBody RestoreRequest restoreRequest) {
        otpService.restoreAccount(restoreRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restore/message")
//    @Operation(summary = "Отправить сообщение на восстановление аккаунта", description = "Данный запрос нужен только для теста регистрации")
    public ResponseEntity<UserResponse> sendRestoreEmail(@RequestParam String email) {
        otpService.sendRestoreMessage(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
