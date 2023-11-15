package evveryday.evveryday.member.domain;

import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import lombok.*;
import javax.persistence.*;
import evveryday.evveryday.member.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Builder
@Table(name="Member")
@Entity
public class MemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAccountNonExpired;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private String mbti;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberGroup> MemberGroups;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 상태를 관리하지 않는다면 true로 고정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 상태를 관리하지 않는다면 true로 고정
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 상태를 관리하지 않는다면 true로 고정
    }

    public static MemberEntity toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder){
        MemberEntity member = MemberEntity.builder()
                .id(memberDto.getId())
                .username(memberDto.getUsername())
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .isAccountNonExpired(memberDto.getIsAccountNonExpired())
                .role(MemberRole.NOT_PERMITTED)
                .mbti(memberDto.getMbti())
                .build();
        return member;
    }
    public void verifyEmail() {
        this.role = MemberRole.USER;
    }

}
