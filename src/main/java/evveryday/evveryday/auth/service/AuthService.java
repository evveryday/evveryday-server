package evveryday.evveryday.auth.service;

import evveryday.evveryday.auth.domain.RefreshToken;
import evveryday.evveryday.auth.domain.RefreshTokenRepository;
import evveryday.evveryday.auth.dto.MemberResponseDto;
import evveryday.evveryday.auth.dto.TokenDto;
import evveryday.evveryday.auth.dto.TokenRequestDto;
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

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;


    ///////     회원가입
    @Transactional
    public MemberResponseDto signup(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        MemberEntity member = memberDto.toMember(passwordEncoder);
        MemberResponseDto memberResponseDto = MemberResponseDto.of(memberRepository.save(member));

        mailService.sendVerificationMail(member.getEmail());

        return memberResponseDto;
    }

    ///////     로그인
    @Transactional
    public TokenDto login(MemberDto memberDto) {
        MemberEntity member = memberRepository.findByEmail(memberDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));


        if (member.getRole() == MemberRole.NOT_PERMITTED) {
            throw new RuntimeException("Your account is not permitted.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = memberDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    ///////     토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    ///////     로그아웃
    @Transactional
    public void logout(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            throw new RuntimeException("Access Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        refreshTokenRepository.delete(refreshToken);
    }


}