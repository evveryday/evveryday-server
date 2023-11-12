package evveryday.evveryday.member.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class AdminController {
    ///////     ADMIN 메인 페이지
    @GetMapping("/admin")
    public String adminPage() {
        return "admin/main";
    }

}