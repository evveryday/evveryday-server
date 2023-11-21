package evveryday.evveryday.smtp.controller;

import evveryday.evveryday.smtp.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge=3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    ///////     이메일 인증
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        mailService.verifyEmail(token);
        return "Email verified.";
    }
}
