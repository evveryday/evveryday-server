package evveryday.evveryday.smtp.service;

import evveryday.evveryday.auth.domain.VerificationToken;
import evveryday.evveryday.auth.domain.VerificationTokenRepository;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MemberRepository memberRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "evveryday3@gmail.com";

    ///////     이메일 형식
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

    ///////     이메일 전송
    public void sendMail(String email, String verificationLink){
        MimeMessage message = createMail(email, verificationLink);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    ///////     인증 이메일 전송
    @Transactional
    public void sendVerificationMail(String email) {
        // 이메일 인증 토큰 생성 및 저장
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, email, LocalDateTime.now().plusMinutes(5));
        verificationTokenRepository.save(verificationToken);

        // 이메일 인증 링크 생성 및 전송
        String verificationLink = "https://www.evveryday.site/auth/verify?token=" + token;
        sendMail(email, verificationLink);
    }

    ///////     이메일 인증
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        MemberEntity member = memberRepository.findByEmail(verificationToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        member.verifyEmail();

        verificationTokenRepository.delete(verificationToken);
    }
}

