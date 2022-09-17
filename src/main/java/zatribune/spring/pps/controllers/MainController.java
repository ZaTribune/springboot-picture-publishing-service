package zatribune.spring.pps.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import zatribune.spring.pps.domain.UserDTO;
import zatribune.spring.pps.domain.UserMapper;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.AppUser;
import zatribune.spring.pps.services.PicService;
import zatribune.spring.pps.services.SecurityService;
import zatribune.spring.pps.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
public class MainController {

    private final SecurityService securityService;
    private final PicService picService;
    private final UserMapper userMapper;

    private boolean logout =false;

    @Autowired
    public MainController(SecurityService securityService, PicService picService, UserMapper userMapper){
        this.securityService=securityService;
        this.picService=picService;
        this.userMapper =userMapper;
    }

    @RequestMapping(value = {"/login/OK"})
    public String loginSuccessful(HttpServletRequest request, Model model) {
        List<Pic> list=picService.getAllByStatus(List.of(PicStatus.APPROVED));
        model.addAttribute(new Pic());//case user choose to upload a pic
        model.addAttribute("pics",list);
        model.addAttribute("categories", PicCategory.values());
        return "index :: BodyFragment";
    }

    @RequestMapping(value = {"/exit"})
    public String logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        logout =true;
        return "redirect:";
    }

    @RequestMapping(value = {"/","/index",""})
    public String getMainPage(Model model){
        log.info("{}:{}",getClass().getSimpleName(),"/index");
        model.addAttribute("hello_message","Welcome to our website!");
        List<Pic> list=picService.getAllByStatus(List.of(PicStatus.APPROVED));

        SecurityContext securityContext= SecurityContextHolder.getContext();

        if (securityContext.getAuthentication()!=null)
            log.info("{}:{}",getClass().getSimpleName(),securityContext.getAuthentication().toString());

        model.addAttribute(new Pic());//case user choose to upload a pic
        model.addAttribute("pics",list);
        model.addAttribute("categories", PicCategory.values());

        if (logout){
            model.addAttribute("logout", true);
        }
        logout =false;
        return "index";
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
        UserService userService=securityService.userService();
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

    @RequestMapping("/modal/{type}")
    public String getModal
            (@PathVariable ModalType type, Model model, @RequestParam(required = false) List<ObjectError> errors) {
        log.info("Now, I'm opening a modal of type {}", type);
        //todo: use Fragmentation =>e.g.  return "fragments/modal :: " + modalType;
        switch (type) {
            case LOGIN:
                model.addAttribute("user",new AppUser());
                model.addAttribute("title", "Login");
                model.addAttribute("info", "Please, enter your credentials.");

                return "fragments/loginModal";
            case INFO:
                model.addAttribute("title", "");
                model.addAttribute("info", "....some information");
                break;
            case DELETE:
                model.addAttribute("title", "Confirm Deleting a pic !");
                model.addAttribute("question", "Are you sure you want to delete this pic?");
                model.addAttribute("info", "....some errors");
                break;
            case ERROR:
                model.addAttribute("title", null);
                model.addAttribute("errors", errors);
                model.addAttribute("info", "....some errors");
                break;
        }

        return "fragments/modal";
    }
}
