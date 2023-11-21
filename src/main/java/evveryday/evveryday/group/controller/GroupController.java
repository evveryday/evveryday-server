package evveryday.evveryday.group.controller;

import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import evveryday.evveryday.group.domain.memberGroup.MemberGroupRepository;
import evveryday.evveryday.group.dto.GroupDto;
import evveryday.evveryday.group.service.GroupService;
import evveryday.evveryday.group.service.S3Service;
import evveryday.evveryday.jwt.TokenProvider;
import evveryday.evveryday.member.domain.MemberEntity;
import evveryday.evveryday.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(maxAge=3600)
@RestController
@AllArgsConstructor
public class GroupController {
    private final S3Service s3Service;
    private final GroupService groupService;
    private final MemberGroupRepository memberGroupRepository;
    private final MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;

    ///////     전체 그룹 중 TOP 3까지만 조회
    @GetMapping("/groups")
    public List<Object[]> top3Groups() {
        return groupService.getTopGroups();
    }

    ///////     전체 그룹 조회
    @GetMapping("/groups/all")
    public Page<Object[]> groupList(@RequestParam(defaultValue = "0") int page) {
        return groupService.getAllGroups(PageRequest.of(page, 5));
    }

    ///////     추천 그룹 조회
    @GetMapping("/member/recommended-groups")
    public List<Object[]> recommendedGroups(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        String userMbti = currentUser.getMbti();
        return groupService.recommendGroupsByMbti(userMbti, currentUser);
    }

    /////// 그룹 검색
    @GetMapping("/group/search")
    public ResponseEntity<?> searchGroups(@RequestParam String keyword) {
        List<GroupEntity> searchResults = groupService.searchGroupsByName(keyword);
        return ResponseEntity.ok(searchResults);
    }

    ///////     그룹 생성
    @PostMapping("/group/new")
    public String execCreateNewGroup(@ModelAttribute GroupDto groupDto,
                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam("days") int days, Model model) {
        GroupEntity groupEntity = null;

        try {
            String originalFileName = s3Service.findOriginalFileName(file);
            String filePath = s3Service.upload(file);
            groupDto.setImageKey(originalFileName);
            groupDto.setImagePath(filePath);
            groupDto.setHeadCount(1);
            LocalDate startDate = groupDto.getStartDate();
            LocalDate expireDate = startDate.plusDays(days);
            groupDto.setExpireDate(expireDate);
            groupEntity = GroupEntity.toEntity(groupDto);
            groupService.saveGroup(groupEntity);

            model.addAttribute("message", "그룹 생성이 완료되었습니다.");
        } catch (IOException e) {
            model.addAttribute("message", "그룹 생성에 실패하였습니다.");
            e.printStackTrace();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        MemberEntity currentUser = memberService.findByEmail(userDetails.getUsername());
        MemberGroup memberGroup = MemberGroup.create(currentUser, groupEntity);

        memberGroupRepository.save(memberGroup);
        return "redirect:/group";
    }

    ///////     그룹 상세 설명
    @GetMapping("/group/description/{groupName}")
    public GroupDto groupInfo(@PathVariable String groupName) {
        GroupEntity groupEntity = groupService.findByName(groupName);
        return GroupDto.toDto(groupEntity);
    }

    ///////     그룹 참여
    @GetMapping("/group/join/{groupName}")
    public String joinGroup(@PathVariable String groupName, HttpServletRequest request, Model model) {
        String token = request.getHeader("Authorization").substring(7);
        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity currentUser = memberService.findByEmail(email);
        GroupEntity groupEntity = groupService.findByName(groupName);
        MemberGroup existingMemberGroup = memberGroupRepository.findByMemberAndGroup(currentUser, groupEntity);
        if (existingMemberGroup != null) {
            model.addAttribute("message", "이미 가입한 그룹입니다.");
            return "redirect:/group";
        }
        groupEntity.setHeadCount(groupEntity.getHeadCount()+1);

        MemberGroup memberGroup = MemberGroup.create(currentUser, groupEntity);
        memberGroupRepository.save(memberGroup);
        return "redirect:/group";
    }

}