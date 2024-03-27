package kz.flurent.methods;

import kz.flurent.client.MailClient;
import kz.flurent.model.request.MailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallApi {

    private final MailClient mailClient;

    public void sendMail(MailRequest mailRequest){
        try {
            mailClient.sendEmail(mailRequest);
            log.info("Invitation has been sent");
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
