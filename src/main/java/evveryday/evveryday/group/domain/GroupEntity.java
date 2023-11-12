package evveryday.evveryday.group.domain;


import evveryday.evveryday.group.domain.memberGroup.MemberGroup;
import evveryday.evveryday.group.dto.GroupDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Builder
@Table(name="Group_table")
@Entity
public class GroupEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private Integer headCount;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column
    private String status;

    @Column
    private LocalDate expireDate;

    @Column(nullable = false)
    private String imageKey;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String imagePath;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<MemberGroup> MemberGroups;


    public static GroupEntity toEntity(GroupDto groupDto){
        GroupEntity group = GroupEntity.builder()
                .id(groupDto.getId())
                .name(groupDto.getName())
                .goal(groupDto.getGoal())
                .headCount(groupDto.getHeadCount())
                .description(groupDto.getDescription())
                .startDate(groupDto.getStartDate())
                .expireDate(groupDto.getExpireDate())
                .imageKey(groupDto.getImageKey())
                .imagePath(groupDto.getImagePath())
                .status(groupDto.getStatus())
                .build();
        return group;
    }


}
