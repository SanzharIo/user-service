package kz.flurent.service.otp;

import kz.flurent.methods.CallApi;
import kz.flurent.model.entity.OtpRequest;
import kz.flurent.model.entity.User;
import kz.flurent.model.request.MailRequest;
import kz.flurent.model.request.RestoreRequest;
import kz.flurent.model.response.OtpResponse;
import kz.flurent.model.response.errors.ErrorCode;
import kz.flurent.model.response.errors.ServiceException;
import kz.flurent.repository.OtpRepository;
import kz.flurent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImp implements OtpService {

    private final CallApi callApi;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OtpResponse checkOtpRequest(String destination, String code) {
        User user = userRepository.findByUsername(destination)
                .orElseThrow(() -> new ServiceException("User doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));

        OtpRequest otpRequest = otpRepository.findByDestinationAndCodeAndUserAndValidIsTrue(destination, code, user)
                .orElseThrow(() -> new ServiceException("Otp doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!otpRequest.isValid()) {
            return new OtpResponse(true, "This otp code is used");
        } else if (isOtpDateValid(otpRequest.getCreatedAt())) {
            otpRequest.setValid(false);
            otpRepository.save(otpRequest);
            return new OtpResponse(true, "This otp code is expired");
        }

        return new OtpResponse(true, null);
    }

    @Override
    @Transactional
    public void sendRestoreMessage(String email) {
        Optional<User> userOptional = userRepository.findByUsername(email);

        if (userOptional.isEmpty()) {
            throw new ServiceException(String.format("Email %s not exist", email), ErrorCode.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();

        String code = createRandomCode(false, true, 4);
        String subject = "Восстановление пароля учетной записи Flurent";
        String message = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <style type="text/css">
                        body, html {
                            margin: 0;
                            padding: 0;
                            width: 100%%;
                            -webkit-text-size-adjust: 100%%;
                            -ms-text-size-adjust: 100%%;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 0 auto;
                            text-align: center;
                        }
                        .button {
                            -webkit-box-sizing: border-box;
                            box-sizing: border-box;
                            color: #fff;
                            width: fit-content;
                            border-radius: 10px;
                            text-align: center;
                            height: 50px;
                            padding: 0.7rem 2.5rem;
                            background-color: black;
                            text-decoration: none;
                            -webkit-transition: -webkit-box-shadow 0.2s ease-out;
                            transition: box-shadow 0.2s ease-out, -webkit-box-shadow 0.2s ease-out;
                        }
                    </style>
                </head>
                <body>
                    <center>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%%">
                            <tr>
                                <td align="center">
                                    <table class="email-container" style='color: #000000'>
                                        <tr>
                                            <td align="center">
                                                <p>Здравствуйте!<br>
                                                Мы получили запрос на восстановление пароля для вашей учетной записи к учетной записи Flurent<br>
                                                Ваш код восстановления учтенной записи %s<br>
                                                Если вы не запрашивали восстановления пароля, проигнорируйте это письмо.<br><br></p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </center>
                </body>
                </html>""", code);

        callApi.sendMail(new MailRequest(email, subject, message));
        OtpRequest otpRequest = new OtpRequest();

        otpRequest.setDestination(email);
        otpRequest.setCode(code);
        user.getOtpRequests().add(otpRequest);
        userRepository.save(user);
    }

    @Override
    public void restoreAccount(RestoreRequest restoreRequest) {
        User user = userRepository.findByUsername(restoreRequest.getDestination())
                .orElseThrow(() -> new ServiceException("User not found", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));

        OtpRequest otpRequest = otpRepository.findByDestinationAndCodeAndUserAndValidIsTrue(restoreRequest.getDestination(), restoreRequest.getOtp(), user)
                .orElseThrow(() -> new ServiceException("This otp doesn't exist", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));
        if (!otpRequest.isValid()) {
            throw new ServiceException("This otp code is used", ErrorCode.INVALID_OTP_USED, HttpStatus.NOT_FOUND);
        } else if (isOtpDateValid(otpRequest.getCreatedAt())) {
            throw new ServiceException("This otp code is expired", ErrorCode.INVALID_OTP_EXPIRED, HttpStatus.NOT_FOUND);
        }

        user.setPassword(passwordEncoder.encode(restoreRequest.getPassword()));
        otpRequest.setValid(false);

        otpRepository.save(otpRequest);
        userRepository.save(user);
    }


    public boolean isOtpDateValid(Date date) {

        long twentyFourHoursInMs = 24 * 60 * 60 * 1000;

        long yourDateMs = date.getTime();

        long currentTimeMs = System.currentTimeMillis();
        return (currentTimeMs - yourDateMs) >= twentyFourHoursInMs;
    }

    public String createRandomCode(boolean useLetters, boolean useNumbers, int length) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

}
