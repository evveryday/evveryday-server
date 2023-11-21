package evveryday.evveryday.group.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<GroupEntity, Long> {
    GroupEntity findByName(String name);
    List<GroupEntity> findByNameContaining(String keyword);
}