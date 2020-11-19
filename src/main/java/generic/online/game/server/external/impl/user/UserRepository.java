package generic.online.game.server.external.impl.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TttUser, Long> {
    TttUser getFirstByUsername(String username);
}
