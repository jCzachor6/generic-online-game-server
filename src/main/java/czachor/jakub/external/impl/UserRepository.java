package czachor.jakub.external.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TttUser, Long> {
}
