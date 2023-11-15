package evveryday.evveryday.member.controller;

import evveryday.evveryday.member.dto.*;
import evveryday.evveryday.member.service.AuthService;
import evveryday.evveryday.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.signup(memberDto));
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return "Email verified.";
    }

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

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto tokenRequestDto) {
        authService.logout(tokenRequestDto);
        return ResponseEntity.ok().build();
    }


}
