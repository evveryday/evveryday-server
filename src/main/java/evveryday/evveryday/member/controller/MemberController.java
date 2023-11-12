package evveryday.evveryday.member.controller;

import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.dto.MemberDto;
import evveryday.evveryday.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    ///////     회원가입!!!!!!!!!!!
    @GetMapping("/join")
    public String memberForm(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "memberForm";
    }

    @PostMapping("/join")
    public String memberForm(@Valid MemberDto memberDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "memberForm";
        }

        try {
            MemberEntity memberEntity = MemberEntity.toEntity(memberDto, passwordEncoder);
            memberService.saveMember(memberEntity, memberDto);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "loginPage";
        }
        return "loginPage";
    }

    ///////     로그인!!!!!!!!!!!!
    @GetMapping("/login")
    public String loginPage() { return "loginPage"; }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요.");
        return "loginPage";
    }

    @PostMapping("/login")
    public String execLogin(){ return "memberPage"; }

    ///////     로그아웃!!!!!!!!!!!
    @GetMapping("/logout")
    public String execLogout(){
        return "redirect:/login";
    }

    ///////     회원 페이지!!!!!!!!!!
    @GetMapping("/member")
    public String memberPage(){
        return "memberPage";
    }

    @PostMapping("/member")
    public String execMember(){
        return "memberPage";
    }



}