package kz.flurent.client;

import kz.flurent.model.request.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mail-service", url = "${clients.mail-service}")
public interface MailClient {

    @PostMapping("/sendgrid/")
    public ResponseEntity<Void> sendEmail(@RequestBody MailRequest mailRequest);

}
