package evveryday.evveryday.group.service;

import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.group.domain.GroupRepository;
import evveryday.evveryday.group.domain.memberGroup.MemberGroupRepository;
import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import evveryday.evveryday.group.dto.GroupDto;
import evveryday.evveryday.member.domain.MemberEntity;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;

    ///////     그룹명으로 그룹 정보 가져오기
    public GroupEntity findByName(String name) {
        return groupRepository.findByName(name);
    }

    ///////     DB에 그룹 저장
    @Transactional
    public void saveGroup(GroupEntity groupEntity) {
        groupRepository.save(groupEntity);
    }

    ///////     전체 그룹 중 인원수 가장 많은 TOP3 그룹 조회
    public List<Object[]> getTopGroups() {
        List<Object[]> groups = StreamSupport.stream(groupRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing((GroupEntity group) -> -group.getHeadCount()))  // MBTI 유사도를 기반으로 정렬
                .limit(3)  // 상위 3개 그룹만 추천
                .map(group -> new Object[] {group.getImagePath(), group.getName(), group.getGoal(), group.getExpireDate(), group.getHeadCount()})
                .collect(Collectors.toList());
        return groups;
    }

    ///////     전체 그룹 조회
    public Page<Object[]> getAllGroups(Pageable pageable) {
        Page<GroupEntity> groupPage = groupRepository.findAll(pageable);  // 페이징 처리된 GroupEntity 데이터 조회
        return groupPage.map(group -> new Object[] {group.getImagePath(), group.getName(), group.getGoal(), group.getExpireDate(), group.getHeadCount()});
    }

    ///////     MBTI에 따른 그룹 추천
    public List<Object[]> recommendGroupsByMbti(String userMbti, MemberEntity currentUser) {
        List<MemberGroup> joinedGroups = memberGroupRepository.findAllByMember(currentUser);
        List<Object[]> groups = StreamSupport.stream(groupRepository.findAll().spliterator(), false)
                .filter(group -> joinedGroups.stream().noneMatch(joinedGroup -> joinedGroup.getGroup().getId().equals(group.getId())))  // 사용자가 가입하지 않은 그룹만 필터링
                .sorted(Comparator.comparing((GroupEntity group) -> -averageMbtiSimilarity(group, userMbti))  // MBTI 유사도를 기반으로 정렬
                        .thenComparing(group -> -group.getHeadCount()))  // 유사도가 동일한 경우, 인원 수가 많은 그룹을 먼저 추천
                .limit(3)  // 상위 3개 그룹만 추천
                .map(group -> new Object[] {group.getImagePath(), group.getName(), group.getGoal(), group.getExpireDate(), group.getHeadCount()})
                .collect(Collectors.toList());
        return groups;
    }

    ///////     사용자-그룹의 MBTI 유사도
    private double averageMbtiSimilarity(GroupEntity group, String userMbti) {
        List<MemberEntity> members = memberGroupRepository.findAllByGroup(group).stream()
                .map(MemberGroup::getMember)
                .collect(Collectors.toList());

        double totalSimilarity = 0.0;
        for (MemberEntity member : members) {
            totalSimilarity += mbtiSimilarity(userMbti, member.getMbti());
        }

        return members.size() > 0 ? totalSimilarity / members.size() : 0;
    }

    ///////     사용자-사용자의 MBTI 유사도
    private double mbtiSimilarity(String mbti1, String mbti2) {
        int matchCount = 0;
        for (int i = 0; i < 4; i++) {
            if (mbti1.charAt(i) == mbti2.charAt(i)) {
                matchCount++;
            }
        }
        return (double) matchCount / 4.0;
    }

    ///////     검색어로 그룹 조회
    public List<GroupEntity> searchGroupsByName(String keyword) {
        return groupRepository.findByNameContaining(keyword);
    }


    public GroupDto convertEntityToDto(GroupEntity groupEntity) {
        return GroupDto.builder()
                .id(groupEntity.getId())
                .name(groupEntity.getName())
                .goal(groupEntity.getGoal())
                .description(groupEntity.getDescription())
                .startDate(groupEntity.getStartDate())
                .expireDate(groupEntity.getExpireDate())
                .imageKey(groupEntity.getImageKey())
                .imagePath(groupEntity.getImagePath())
                .build();
    }
}
