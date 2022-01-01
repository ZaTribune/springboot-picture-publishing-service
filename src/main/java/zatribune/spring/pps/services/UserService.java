package zatribune.spring.pps.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import zatribune.spring.pps.data.entities.User;


public interface UserService extends UserDetailsService {
    void save(User user);
    void saveToDefaults(User user);
}
