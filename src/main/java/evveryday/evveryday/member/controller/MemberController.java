package evveryday.evveryday.member.controller;

import evveryday.evveryday.jwt.TokenProvider;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@CrossOrigin(maxAge=3600)
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;

    ///////     Member 페이지 조회
    @PostMapping("/")
    public ResponseEntity<?> execMember(){
        return ResponseEntity.ok("Member page");
    }

    ///////     사용자의 참가 중인 그룹수 조회
    @GetMapping("/member/myGroups/count/processing")
    public int myProcessGroupCount(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        return memberService.getProcessingGroupCount(currentUser);
    }

    ///////     사용자의 만료된 그룹수 조회
    @GetMapping("/member/myGroups/count/expired")
    public int myExpiredGroupCount(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        return memberService.getExpiredGroupCount(currentUser);
    }

    ///////     사용자의 진행 중인 그룹 조회
    @GetMapping("/member/myGroups/processing")
    public List<Object[]> myProcessGroupList(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        return memberService.getProcessingGroups(currentUser);
    }

    ///////     사용자의 만료된 그룹 조회
    @GetMapping("/member/myGroups/expired")
    public List<Object[]> myExpiredGroupList(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        return memberService.getExpiredGroups(currentUser);
    }

}
