package evveryday.evveryday.group.domain.memberGroup;

import evveryday.evveryday.group.domain.BaseTimeEntity;
import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.member.domain.MemberEntity;
import lombok.*;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Builder
@Table(name="MemberGroup")
@Entity
public class MemberGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "memberGroup_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;



    public static MemberGroup create(MemberEntity member, GroupEntity group) {
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setMember(member);
        memberGroup.setGroup(group);

        return memberGroup;
    }


}
