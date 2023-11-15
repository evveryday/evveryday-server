package evveryday.evveryday.smtp.controller;

import evveryday.evveryday.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final AuthService authService;

    @PostMapping("/join/mail")
    public String MailSend(@RequestParam("email") String email){
        authService.sendVerificationMail(email);
        return "Verification mail sent.";
    }
}
