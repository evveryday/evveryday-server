package evveryday.evveryday.group.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<GroupEntity, Long> {
    GroupEntity findByName(String name);
}