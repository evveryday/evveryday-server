package evveryday.evveryday.smtp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "wisejohn950330@gmail.com";
    private static int number;

    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Evveryday 이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail){
        //MimeMessage message = CreateMail(mail);
        //javaMailSender.send(message);
        MimeMessage message = null;
        try {
            message = CreateMail(mail);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // 적절한 오류 메시지를 로그로 남깁니다.
            System.out.println("Failed to send email: " + e.getMessage());
            // 이메일 전송에 실패했음을 나타내는 값을 반환합니다.
            return -1;
        }
        return number;
    }
}
