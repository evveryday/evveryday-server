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

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class AdminController {


    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
/*
    ///////     로그인!!!!!!!!!!!!
    @GetMapping("/login/admin")
    public String loginPage() { return "admin/loginPage"; }

    @GetMapping(value = "/login/admin/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호를 확인해주세요.");
        return "admin/loginPage";
    }

    @PostMapping("/login/admin")
    public String execLogin(){ return "redirect:/admin"; }



    ///////     로그아웃!!!!!!!!!!!
    @GetMapping("/admin/logout")
    public String Logout(){
        return "redirect:/login";
    }

*/
    ///////     ADMIN 메인 페이지
    @GetMapping("/admin")
    public String adminPage() {
        return "admin/main";
    }

}