package evveryday.evveryday.group.domain.done;

import evveryday.evveryday.group.domain.GroupEntity;
import evveryday.evveryday.member.domain.MemberEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Builder
@Table(name="Done")
@Entity
public class Done {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @Column
    private LocalDateTime createdDate;

    @Column
    private String status = "A";

}