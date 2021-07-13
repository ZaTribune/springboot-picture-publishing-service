package zatribune.spring.pps.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zatribune.spring.pps.data.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
