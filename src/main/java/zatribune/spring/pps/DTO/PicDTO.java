package zatribune.spring.pps.DTO;

import lombok.Getter;
import lombok.Setter;
import zatribune.spring.pps.data.entities.Category;
import zatribune.spring.pps.data.entities.User;

@Getter
@Setter
public class PicDTO {


    private User user;

    private String description;

    private Category category;

    private String base64Image;

}
