package evveryday.evveryday.member.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminController {
    @GetMapping("/admin")
    public ResponseEntity<String> adminPage() {
        return ResponseEntity.ok("Admin main page");
    }
}
