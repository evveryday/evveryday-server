package evveryday.evveryday.auth.controller;

import evveryday.evveryday.auth.dto.MemberResponseDto;
import evveryday.evveryday.auth.dto.TokenDto;
import evveryday.evveryday.auth.dto.TokenRequestDto;
import evveryday.evveryday.member.dto.*;
import evveryday.evveryday.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(maxAge=3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    ///////     회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.signup(memberDto));
    }

    ///////     로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        try {
            return ResponseEntity.ok(authService.login(memberDto));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "Caught exception of type: " + e.getClass().getName();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", errorMessage));
        }
    }

    ///////     토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    ///////     로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        authService.logout(tokenRequestDto);
        return ResponseEntity.ok().build();
    }
}
