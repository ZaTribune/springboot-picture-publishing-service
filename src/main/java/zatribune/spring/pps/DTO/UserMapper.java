package zatribune.spring.pps.DTO;


import org.springframework.stereotype.Component;
import zatribune.spring.pps.data.entities.User;

@Component
public class UserMapper {

    public User toUser(UserDTO input){
        User user=new User();
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword());
        return user;
    }

}
