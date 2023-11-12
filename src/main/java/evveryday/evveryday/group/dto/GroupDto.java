package evveryday.evveryday.group.dto;

import evveryday.evveryday.group.domain.GroupEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupDto{
    private Long id;
    private String name;
    private String goal;
    private Integer headCount;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private LocalDate expireDate;
    private String imageKey;
    private String imagePath;
    private String status = "A";

    public static GroupDto toDto(GroupEntity groupEntity) {
        return GroupDto.builder()
                .name(groupEntity.getName())
                .goal(groupEntity.getGoal())
                .headCount(groupEntity.getHeadCount())
                .description(groupEntity.getDescription())
                .startDate(groupEntity.getStartDate())
                .expireDate(groupEntity.getExpireDate())
                .imageKey(groupEntity.getImageKey())
                .imagePath(groupEntity.getImagePath())
                .status(groupEntity.getStatus())
                .build();
    }

    @Builder
    public GroupDto(Long id, String name, String goal, Integer headCount, String description, LocalDate startDate, LocalDate expireDate, String imageKey, String imagePath, String status) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.headCount = headCount;
        this.description = description;
        this.startDate = startDate;
        this.expireDate = expireDate;
        this.imageKey = imageKey;
        this.imagePath = imagePath;
        this.status = status;
    }
}
