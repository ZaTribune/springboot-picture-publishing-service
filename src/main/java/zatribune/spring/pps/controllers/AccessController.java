package zatribune.spring.pps.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zatribune.spring.pps.DTO.UserDTO;
import zatribune.spring.pps.DTO.UserMapper;
import zatribune.spring.pps.services.SecurityService;
import zatribune.spring.pps.services.UserDetailsService;

import javax.validation.Valid;


@Slf4j
@Controller
public class AccessController {

    private final SecurityService securityService;
    private final UserMapper userMapper;

    @Autowired
    public AccessController(SecurityService securityService, UserMapper userMapper){
        this.securityService=securityService;
        this.userMapper =userMapper;
    }


    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("user", new UserDTO());
        return "/registration";
    }

    @PostMapping("/registration/new")
    public String registration(@ModelAttribute("user")@Valid UserDTO user, BindingResult bindingResult) {

        //customized check
        if (!user.getPassword().equals(user.getPasswordConfirm())){
            log.error("Passwords don't match.");
            bindingResult.rejectValue("passwordConfirm","","Password Fields Must match.");
        }
        UserDetailsService userService=securityService.userService();
        if(userService.loadUserByUsername(user.getUsername())!=null){
            log.error("A User with this email already exists.");
            bindingResult.rejectValue("username","","A User with this email already exists.");
        }
        if (bindingResult.hasErrors()){
            return "/registration :: RegistrationFragment";
        }
        userService.saveToDefaults(userMapper.toUser(user));
        //todo: add account confirmation
        //securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return "redirect:/index";
    }
}
