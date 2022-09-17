package zatribune.spring.pps.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zatribune.spring.pps.data.entities.AppUser;
import zatribune.spring.pps.data.entities.Role;
import zatribune.spring.pps.data.repositories.UserRepository;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("find by username "+username);

        return userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(username));
    }

    public void save(AppUser appUser) {
        //todo: check authorities
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));

        appUser.grantAuthority(Role.USER);

        userRepository.save(appUser);
    }


    @Transactional//to fix a detached entity passed to persist ...Role
    public void saveToDefaults(AppUser appUser) {
        //todo: check authorities
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));

        appUser.grantAuthority(Role.USER);

        //todo: those requests need to be confirmed via one of these 3 methods
        //        1- by the admin
        //        2- automatically by emails
        //        3- using Spring social

        userRepository.save(appUser);
    }
}
