package evveryday.evveryday.smtp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "evveryday3@gmail.com";

    public MimeMessage createMail(String email, String verificationLink){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Evveryday 이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 링크입니다. 해당 링크에 접속하여 인증을 완료해주세요." + "</h3>";
            body += "<h1><a href=\"" + verificationLink + "\">인증 링크</a></h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public void sendMail(String email, String verificationLink){
        MimeMessage message = createMail(email, verificationLink);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}

