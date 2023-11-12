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

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberEntity findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    ///////     회원가입
    public MemberEntity saveMember(MemberEntity memberEntity, MemberDto memberDto) {
        validateDuplicateMember(memberEntity);
        return memberRepository.save(memberEntity);
    }

    private void validateDuplicateMember(MemberEntity memberEntity) {
        MemberEntity findMember = memberRepository.findByEmail(memberEntity.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    ///////     로그인
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


}
