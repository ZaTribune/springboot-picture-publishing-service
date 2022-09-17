package zatribune.spring.pps.domain;

import lombok.Getter;
import lombok.Setter;
import zatribune.spring.pps.data.entities.PicCategory;
import zatribune.spring.pps.data.entities.AppUser;

@Getter
@Setter
public class PicDTO {

    //todo: to replace Pic on requests/responses

    private AppUser appUser;

    private String description;

    private PicCategory picCategory;


}
