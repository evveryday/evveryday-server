package evveryday.evveryday.member.dto;

import evveryday.evveryday.member.domain.MemberEntity;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private String email;

    public static MemberResponseDto of(MemberEntity member) {
        return new MemberResponseDto(member);
    }

    private MemberResponseDto(MemberEntity member) {
        this.email = member.getEmail();
    }
}
