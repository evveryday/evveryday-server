package evveryday.evveryday.member.service;

import evveryday.evveryday.jwt.TokenProvider;
import evveryday.evveryday.member.domain.*;
import evveryday.evveryday.member.dto.*;
import evveryday.evveryday.smtp.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public MemberResponseDto signup(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        MemberEntity member = memberDto.toMember(passwordEncoder);
        MemberResponseDto memberResponseDto = MemberResponseDto.of(memberRepository.save(member));

        // 이메일 인증 링크 생성 및 전송
        sendVerificationMail(member.getEmail());

        return memberResponseDto;
    }

    @Transactional
    public void sendVerificationMail(String email) {
        // 이메일 인증 토큰 생성 및 저장
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, email, LocalDateTime.now().plusMinutes(5));
        verificationTokenRepository.save(verificationToken);

        // 이메일 인증 링크 생성 및 전송
        String verificationLink = "http://localhost:8080/auth/verify?token=" + token;
        mailService.sendMail(email, verificationLink);
    }

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

    @Transactional
    public TokenDto login(MemberDto memberDto) {
        MemberEntity member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));


        if (member.getRole() == MemberRole.NOT_PERMITTED) {
            throw new RuntimeException("Your account is not permitted.");
        }
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberDto.toAuthentication();

        // 사용자 비밀번호 체크
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // Member ID 를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // DB 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public void logout(TokenRequestDto tokenRequestDto) {
        // Access Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            throw new RuntimeException("Access Token 이 유효하지 않습니다.");
        }

        // Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // Member ID를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // Refresh Token 삭제
        refreshTokenRepository.delete(refreshToken);
    }


}