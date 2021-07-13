package zatribune.spring.pps.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zatribune.spring.pps.data.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findDistinctByUsername(String username);
}
