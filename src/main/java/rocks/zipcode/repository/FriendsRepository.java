package rocks.zipcode.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Friends;

/**
 * Spring Data JPA repository for the Friends entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    @Query("select friends from Friends friends where friends.user.login = ?#{principal.username}")
    List<Friends> findByUserIsCurrentUser();
}
