package zatribune.spring.pps.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import zatribune.spring.pps.data.entities.*;
import zatribune.spring.pps.data.repositories.PicRepository;
import zatribune.spring.pps.data.repositories.RoleRepository;
import zatribune.spring.pps.data.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PicRepository picRepository;

    @Autowired
    public DevBootstrap(UserRepository userRepository, RoleRepository roleRepository, PicRepository picRepository) {
        log.debug(getClass().getSimpleName() + ":I'm at the Bootstrap phase.");
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.picRepository = picRepository;
    }

    @Override
    public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {
        clearOldData();
        initData();
    }

    void clearOldData() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        picRepository.deleteAll();
    }

    @Transactional
    void initData() {
        log.info("initData()");

        Role roleAdmin = new Role();
        roleAdmin.setName(RoleEnum.ROLE_ADMIN.name());

        Role roleUser = new Role();
        roleUser.setName(RoleEnum.ROLE_USER.name());

        Pic pic1 = new Pic();
        pic1.setCategory(Category.living_thing);
        pic1.setPath("cat.png");
        pic1.setStatus(PicStatus.APPROVED);
        pic1.setDescription("Be honest! it's better than a dog.");
        //pic1.setUser(user);

        Pic pic2 = new Pic();
        pic2.setCategory(Category.machine);
        pic2.setPath("f22.png");
        pic2.setStatus(PicStatus.APPROVED);
        pic2.setDescription("King Raptor is here, If you like it ,leave a comment below.\nOps!! no comment section available.");
        //pic2.setUser(user);

        Pic pic3 = new Pic();
        pic3.setCategory(Category.nature);
        pic3.setPath("waterfall.png");
        pic3.setStatus(PicStatus.APPROVED);
        pic3.setDescription("Love is all, a waterfall.\npulls u in, takes u down.\nIt's a sad affair");
        //pic3.setUser(user);

        User admin = new User();
        admin.setUsername("admin@pps.com");
        admin.setPassword("{pbkdf2.2018}1f1fab5f4176f86513f10136f70d4984ef97860490176f501843bb503489");//admin123
        admin.setRoles(Set.of(roleAdmin));//user123
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);

        User user = new User();
        user.setUsername("muhammad.ali@pps.com");
        user.setPassword("{pbkdf2.2018}5905ba87b762cad9626cbaa59777af0ef3a8bd5fdb08a4b0974cf8f5bb8f");//user123
        user.setRoles(Set.of(roleUser));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        pic1.setUser(user);
        pic2.setUser(user);
        pic3.setUser(user);
        user.setPics(Set.of(pic1,pic2,pic3));

        userRepository.saveAll(List.of(admin, user));
    }
}
