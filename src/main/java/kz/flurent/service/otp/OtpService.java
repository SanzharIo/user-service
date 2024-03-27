package kz.flurent.service.otp;


import kz.flurent.model.request.RestoreRequest;
import kz.flurent.model.response.OtpResponse;

public interface OtpService {

    OtpResponse checkOtpRequest(String destination, String code);
    void sendRestoreMessage(String email);
    void restoreAccount(RestoreRequest restoreRequest);


}
