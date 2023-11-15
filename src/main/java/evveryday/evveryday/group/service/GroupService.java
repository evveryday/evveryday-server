package evveryday.evveryday.group.service;

import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.group.domain.GroupRepository;
import evveryday.evveryday.group.domain.memberGroup.MemberGroupRepository;
import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import evveryday.evveryday.group.dto.GroupDto;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.domain.MemberRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public GroupEntity findByName(String name) {
        return groupRepository.findByName(name);
    }

    @Transactional
    public void saveGroup(GroupEntity groupEntity) {
        groupRepository.save(groupEntity);
    }

    public Page<GroupDto> getAllGroups(Pageable pageable) {
        Page<GroupEntity> groupEntities = groupRepository.findAll(pageable);
        List<GroupDto> groupDtos = groupEntities.stream().map(groupEntity -> {
            GroupDto groupDto = this.convertEntityToDto(groupEntity);
            return groupDto;
        }).collect(Collectors.toList());

        return new PageImpl<>(groupDtos, pageable, groupEntities.getTotalElements());
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

    public List<GroupEntity> recommendGroupsByMbti(String mbti, MemberEntity currentUser) {
        List<GroupEntity> groups = StreamSupport.stream(groupRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparingLong(group ->
                        -countMembersByMbti(group, mbti)))
                .collect(Collectors.toList());

        List<MemberGroup> joinedGroups = memberGroupRepository.findByMember(currentUser);
        groups.removeIf(group -> joinedGroups.stream()
                .anyMatch(joinedGroup -> joinedGroup.getGroup().getId().equals(group.getId())));

        return groups;
    }

    private long countMembersByMbti(GroupEntity group, String mbti) {
        return memberGroupRepository.findAllByGroup(group).stream()
                .map(MemberGroup::getMember)
                .filter(member -> member.getMbti().equals(mbti))
                .count();
    }

    public List<GroupEntity> searchGroupsByName(String keyword) {
        return groupRepository.findByNameContaining(keyword);
    }
}