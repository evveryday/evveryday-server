package evveryday.evveryday.challenge.controller;

import evveryday.evveryday.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

//    @PostMapping("/done")
//    public ResponseEntity<Long> createDone()
}
