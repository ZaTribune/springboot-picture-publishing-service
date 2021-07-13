package zatribune.spring.pps.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zatribune.spring.pps.data.entities.Role;
import zatribune.spring.pps.data.entities.User;
import zatribune.spring.pps.data.repositories.RoleRepository;
import zatribune.spring.pps.data.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("find by username "+username);

        User user = userRepository.findDistinctByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        //Cast the current Mono produced type into a target produced type.
        return user;
    }

    public void save(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role= roleRepository.findAll().iterator().next();
        if (role!=null)
            user.setRoles(new HashSet<>(List.of(role)));

        userRepository.save(user);
    }

    public void saveToDefaults(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role= roleRepository.findAll().iterator().next();
        if (role!=null)
            user.setRoles(new HashSet<>(List.of(role)));

        user.setEnabled(false);
        user.setAccountNonLocked(false);
        user.setAccountNonExpired(false);
        user.setCredentialsNonExpired(false);
        userRepository.save(user);
    }


}
