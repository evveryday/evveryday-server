package evveryday.evveryday.member.service;


import evveryday.evveryday.group.domain.GroupRepository;
import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import evveryday.evveryday.group.domain.memberGroup.MemberGroupRepository;
import lombok.RequiredArgsConstructor;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.domain.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;

    public MemberEntity findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    ///////     사용자의 진행 중인 그룹 수
    public int getProcessingGroupCount(MemberEntity currentUser){
        List<Object[]> processingGroups = getProcessingGroups(currentUser);
        return processingGroups.size();  // 진행 중인 그룹의 수를 반환
    }

    ///////     사용자의 만료된 그룹 수
    public int getExpiredGroupCount(MemberEntity currentUser){
        List<Object[]> expiredGroups = getExpiredGroups(currentUser);
        return expiredGroups.size();  // 만료된 그룹의 수를 반환
    }

    ///////     사용자의 진행 중인 그룹 조회
    public List<Object[]> getProcessingGroups(MemberEntity currentUser){
        List<MemberGroup> memberGroups = memberGroupRepository.findAllByMember(currentUser);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        return memberGroups.stream()
                .map(MemberGroup::getGroup)
                .filter(group -> group.getExpireDate().isAfter(yesterday))  // 만료되지 않은 그룹만 필터링
                .map(group -> new Object[] {group.getName(), group.getGoal(), group.getExpireDate()})
                .collect(Collectors.toList());
    }

    ///////     사용자의 만료된 그룹 조회
    public List<Object[]> getExpiredGroups(MemberEntity currentUser){

        List<MemberGroup> memberGroups = memberGroupRepository.findAllByMember(currentUser);
        LocalDate today = LocalDate.now();
        return memberGroups.stream()
                .map(MemberGroup::getGroup)
                .filter(group -> group.getExpireDate().isBefore(today))  // 만료된 그룹만 필터링
                .map(group -> new Object[] {group.getName(), group.getGoal(), group.getExpireDate()})
                .collect(Collectors.toList());
    }

}


