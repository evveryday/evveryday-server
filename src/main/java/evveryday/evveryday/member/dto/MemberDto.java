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

    public MemberEntity toEntity(){
        MemberEntity build = MemberEntity.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .isAccountNonExpired(isAccountNonExpired)
                .build();
        return build;
    }
    @Builder
    public MemberDto(Long id, String username, String email, String password, /*LocalDateTime createdDate, LocalDateTime modifiedDate, */Boolean isAccountNonExpired) {
        this.id = id;
        this.username = username;
        this.email=email;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
    }
}
