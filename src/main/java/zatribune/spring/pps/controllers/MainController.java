package zatribune.spring.pps.controllers;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import zatribune.spring.pps.data.entities.Category;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.User;
import zatribune.spring.pps.exceptions.NotFoundException;
import zatribune.spring.pps.services.PicService;
import zatribune.spring.pps.utils.ImageMapper;
import zatribune.spring.pps.utils.PropertiesExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Controller
public class MainController {
    private final PicService picService;
    private final ImageMapper imageMapper;
    private boolean logout =false;

    @Autowired
    public MainController(PicService picService,ImageMapper imageMapper){
        this.picService=picService;
        this.imageMapper=imageMapper;
    }

    @RequestMapping(value = {"/exit"})
    public String logout(HttpServletRequest request, HttpServletResponse response) {
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
        List<Pic>list=picService.getAllByStatus(List.of(PicStatus.APPROVED));

        SecurityContext securityContext= SecurityContextHolder.getContext();

        if (securityContext.getAuthentication()!=null)
        log.info("{}:{}",getClass().getSimpleName(),securityContext.getAuthentication().toString());

        model.addAttribute(new Pic());//case user choose to upload a pic
        model.addAttribute("pics",list);
        model.addAttribute("categories", Category.values());

        if (logout){
            model.addAttribute("logout", true);
        }
        logout =false;
        return "index";
    }

    @RequestMapping(value = "/pics")
    public String getPicsFragment(Model model){
        log.info("{}:{}",getClass().getSimpleName(),"/pics");
        List<Pic>list=picService.getAllByStatus(List.of(PicStatus.APPROVED));
        log.info("{}:{}",getClass().getSimpleName(),list.size());

        model.addAttribute(new Pic());//case user choose to upload a pic
        model.addAttribute("pics",list);
        model.addAttribute("categories", Category.values());

        return "/home/pics";
    }

    @PostMapping(value = "/pics/add")
    public RedirectView addPic(@ModelAttribute("pic")@Valid Pic pic, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        log.info("{}:{}",getClass().getSimpleName(),"/pics/add");
        String fileName=null;
        if (multipartFile.getOriginalFilename()!=null) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            log.info("{}:{}", getClass().getSimpleName(), fileName);
        }
        //todo: your picture will be reviewed by our admins before being approved ,stay toned.

        imageMapper.saveFile(PropertiesExtractor.FILE_SERVER_PATH, fileName, multipartFile);
        pic.setStatus(PicStatus.BENDING);
        pic.setPath(fileName);
        picService.save(pic);
        return new RedirectView("/index", true);//return to home page
    }

    @GetMapping("/pic/image/{id}")
    public void showProductImage(@PathVariable Long id, HttpServletResponse response)
            throws IOException, NotFoundException {
        response.setContentType("image/jpeg"); // Or whatever format you wanna use

        Pic product = picService.getById(id).orElseThrow(()->new NotFoundException(id));

        InputStream is = new ByteArrayInputStream(imageMapper.getImageUnWrapped(product.getPath()));
        IOUtils.copy(is, response.getOutputStream());
    }

    @RequestMapping("/modal/{type}")
    public String getModal
            (@PathVariable ModalType type, Model model, @RequestParam(required = false) List<ObjectError> errors) {
        log.info("Now, I'm opening a modal of type {}", type);
        //todo: use Fragmentation =>e.g.  return "fragments/modal :: " + modalType;
        switch (type) {
            case LOGIN:
                model.addAttribute("user",new User());
                model.addAttribute("title", "Login");
                model.addAttribute("info", "Please, enter your credentials.");
                break;
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
