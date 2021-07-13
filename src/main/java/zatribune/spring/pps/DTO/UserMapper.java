package zatribune.spring.pps.DTO;


import org.springframework.stereotype.Component;
import zatribune.spring.pps.data.entities.User;

@Component
public class UserMapper {

    public User toUser(UserDTO input){
        User user=new User();
        user.setUsername(input.getUsername());
        user.setPassword(input.getPassword());

        //todo: those requests need to be confirmed via one of these 3 methods
        //        1- by the admin
        //        2- automatically by emails
        //        3- using Spring social
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        return user;
    }

}
