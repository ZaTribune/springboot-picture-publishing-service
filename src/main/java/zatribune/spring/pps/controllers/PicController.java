package zatribune.spring.pps.controllers;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.data.entities.User;
import zatribune.spring.pps.exceptions.NotFoundException;
import zatribune.spring.pps.services.PicService;
import zatribune.spring.pps.utils.ImageMapper;
import zatribune.spring.pps.utils.PropertiesExtractor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Controller
public class PicController {
    private final PicService picService;
    private final ImageMapper imageMapper;


    @Autowired
    public PicController(PicService picService, ImageMapper imageMapper){
        this.picService=picService;
        this.imageMapper=imageMapper;
    }

    @RequestMapping(value = "/pics")
    public String getPicsFragment(Model model){
        log.info("{}:{}",getClass().getSimpleName(),"/pics");
        List<Pic>list=picService.getAllByStatus(List.of(PicStatus.APPROVED));
        log.info("{}:{}",getClass().getSimpleName(),list.size());

        model.addAttribute(new Pic());//case user choose to upload a pic
        model.addAttribute("pics",list);
        model.addAttribute("categories", PicCategory.values());

        return "/home/pics";
    }

    @RequestMapping(value = "/pics/pending")
    public String getPendingPicsFragment(Model model){
        log.info("{}:{}",getClass().getSimpleName(),"/pics");
        List<Pic>list=picService.getAllByStatus(List.of(PicStatus.PENDING));

        log.info("{}:{}",getClass().getSimpleName(),list.size());

        model.addAttribute("pics",list);
        return "/home/pendingPics";
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
        pic.setStatus(PicStatus.PENDING);
        pic.setPath(fileName);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        pic.setUser(user);

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

    @GetMapping("/pics/pendingPics/approve/{id}")
    public String approvePic(@PathVariable("id") Long id, Model model){

        Pic product = picService.getById(id).orElseThrow(()->new NotFoundException(id));

        product.setStatus(PicStatus.APPROVED);
        picService.save(product);

        model.addAttribute("pics",picService.getAllByStatus(List.of(PicStatus.PENDING)));

        return "/home/pendingPics";
    }

    @GetMapping("/pics/pendingPics/decline/{id}")
    public String declinePic(@PathVariable("id") Long id, Model model){
        Pic product = picService.getById(id).orElseThrow(()->new NotFoundException(id));
        product.setStatus(PicStatus.DECLINED);
        picService.save(product);
        model.addAttribute("pics",picService.getAllByStatus(List.of(PicStatus.PENDING)));
        return "/home/pendingPics";
    }

}
