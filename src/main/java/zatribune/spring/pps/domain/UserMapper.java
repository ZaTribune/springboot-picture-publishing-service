package zatribune.spring.pps.domain;


import org.springframework.stereotype.Component;
import zatribune.spring.pps.data.entities.AppUser;

@Component
public class UserMapper {

    public AppUser toUser(UserDTO input){
        AppUser appUser =new AppUser();
        appUser.setUsername(input.getUsername());
        appUser.setPassword(input.getPassword());
        return appUser;
    }

}
