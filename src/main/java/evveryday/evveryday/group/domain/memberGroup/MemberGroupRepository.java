package evveryday.evveryday.group.domain.memberGroup;

import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {

    List<MemberGroup> findAllByGroup(GroupEntity group);
    List<MemberGroup> findAllByMember(MemberEntity member);
    MemberGroup findByMemberAndGroup(MemberEntity member, GroupEntity group);


}
