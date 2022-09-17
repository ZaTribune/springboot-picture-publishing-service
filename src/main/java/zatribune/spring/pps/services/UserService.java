package zatribune.spring.pps.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import zatribune.spring.pps.data.entities.AppUser;


public interface UserService extends UserDetailsService {
    void save(AppUser appUser);
    void saveToDefaults(AppUser appUser);
}
