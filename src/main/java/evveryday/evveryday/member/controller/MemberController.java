package evveryday.evveryday.member.controller;

import evveryday.evveryday.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    @PostMapping("/")
    public ResponseEntity<?> execMember(){
        return ResponseEntity.ok("Member page");
    }
}
