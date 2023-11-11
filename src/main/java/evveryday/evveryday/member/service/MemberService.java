package evveryday.evveryday.member.service;


import evveryday.evveryday.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.domain.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    /*private MemberEntity memberEntity;

    public LocalDateTime loadDate(MemberDto memberDto) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(Locale.KOREA);
        return currentDate;
    }
    @Autowired
    private MemberRepository memberRepository;
    public void savePost(MemberDto memberDto) {
        memberRepository.save(memberEntity.toEntity(memberDto, passwordEncoder));
    }*/
    ///////     회원가입!!!!!!!!!!!!!!!
    public MemberEntity saveMember(MemberEntity memberEntity, MemberDto memberDto) {
        /*if (memberRepository.findByUsername(memberEntity.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 유저네임입니다.");
        }*/
        validateDuplicateMember(memberEntity);
        //savePost(memberDto);
        return memberRepository.save(memberEntity);
    }

    private void validateDuplicateMember(MemberEntity memberEntity) {
        MemberEntity findMember = memberRepository.findByEmail(memberEntity.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /*public boolean emailAuth(String email) {

        Optional<MemberEntity> optionalMember =
                memberRepository.findByEmail(email);

        if (!optionalMember.isPresent()) {
            return false;
        }

        String uuid = UUID.randomUUID().toString();

        //build pattern
        MemberEntity member = Member.builder()
                .email(parameter.getEmail())
                .username(parameter.getUsername())
                .password(parameter.getPassword())
                .createdDate(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);

        return true;
    }
*/
    ///////     로그인!!!!!!!!!!!!!!!!
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    ///////     로그아웃!!!!!!!!!!!!!!

}
