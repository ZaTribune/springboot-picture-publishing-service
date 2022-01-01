package zatribune.spring.pps.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zatribune.spring.pps.data.entities.Role;
import zatribune.spring.pps.data.entities.RoleEnum;
import zatribune.spring.pps.data.entities.User;
import zatribune.spring.pps.data.repositories.RoleRepository;
import zatribune.spring.pps.data.repositories.UserRepository;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("find by username "+username);

        //throwing this exception will result in loading the default spring security login page
        //if (user == null)
            //throw new UsernameNotFoundException(username);

        return userRepository.findDistinctByUsername(username);
    }

    public void save(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role= roleRepository.findDistinctByName(RoleEnum.ROLE_USER.name());
        if (role!=null)
            user.setRoles(Set.of(role));

        userRepository.save(user);
    }


    @Transactional//to fix a detached entity passed to persist ...Role
    public void saveToDefaults(User user) {
        //todo: check authorities
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role= roleRepository.findDistinctByName(RoleEnum.ROLE_USER.name());
        if (role!=null)
            user.setRoles(Set.of(role));

        System.out.println("xxx "+ Objects.requireNonNull(role).getName());

        //todo: those requests need to be confirmed via one of these 3 methods
        //        1- by the admin
        //        2- automatically by emails
        //        3- using Spring social

        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        userRepository.save(user);
    }
}
