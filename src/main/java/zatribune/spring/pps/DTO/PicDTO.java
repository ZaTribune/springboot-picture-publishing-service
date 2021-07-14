package zatribune.spring.pps.DTO;

import lombok.Getter;
import lombok.Setter;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.User;

@Getter
@Setter
public class PicDTO {

    //todo: to replace Pic on requests/responses

    private User user;

    private String description;

    private PicCategory picCategory;


}
