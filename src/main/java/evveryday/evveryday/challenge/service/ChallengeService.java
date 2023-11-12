package evveryday.evveryday.challenge.service;

import evveryday.evveryday.group.domain.done.DoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {
    private final DoneRepository doneRepository;

}
