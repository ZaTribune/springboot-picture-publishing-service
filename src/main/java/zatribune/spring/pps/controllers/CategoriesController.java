package zatribune.spring.pps.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import zatribune.spring.pps.data.entities.Pic;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.PicStatus;
import zatribune.spring.pps.services.PicService;
import zatribune.spring.pps.utils.ImageMapper;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class CategoriesController {

    private final PicService picService;
    private final ImageMapper imageMapper;



    @RequestMapping(value = "/categories")
    public String getPicsFragment(Model model){
        log.info("{}:{}",getClass().getSimpleName(),"/categories");

        model.addAttribute("categories", PicCategory.values());

        return "/home/categories";
    }


}
