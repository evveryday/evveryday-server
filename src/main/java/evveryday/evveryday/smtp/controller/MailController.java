package evveryday.evveryday.smtp.controller;

import evveryday.evveryday.smtp.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/join/mail")
    public String MailSend(@RequestParam("email") String email){
        int number = mailService.sendMail(email);
        String num = "" + number;
        return num;
    }

}
