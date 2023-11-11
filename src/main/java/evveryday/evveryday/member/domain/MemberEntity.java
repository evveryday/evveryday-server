package evveryday.evveryday.member.domain;

import lombok.*;

import javax.persistence.*;

import evveryday.evveryday.member.dto.MemberDto;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Builder
@Table(name="Member")
@Entity
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAccountNonExpired;

    @Enumerated(EnumType.STRING)
    private MemberRole role;


    public static MemberEntity toEntity(MemberDto memberDto, PasswordEncoder passwordEncoder){
        MemberEntity member = MemberEntity.builder()
                .id(memberDto.getId())
                .username(memberDto.getUsername())
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                /*.createdDate(memberDto.getCreatedDate())
                .modifiedDate(memberDto.getModifiedDate())
                */.isAccountNonExpired(memberDto.getIsAccountNonExpired())
                .role(MemberRole.USER)
                .build();
        return member;
    }
}
