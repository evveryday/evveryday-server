package evveryday.evveryday.member.dto;

import evveryday.evveryday.member.domain.MemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean isAccountNonExpired = true;
    private String mbti;

    /*public MemberEntity toEntity(){
        MemberEntity build = MemberEntity.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .isAccountNonExpired(isAccountNonExpired)
                .mbti(mbti)
                .build();
        return build;
    }*/
    @Builder
    public MemberDto(Long id, String username, String email, String password, Boolean isAccountNonExpired, String mbti) {
        this.id = id;
        this.username = username;
        this.email=email;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.mbti = mbti;
    }
}
