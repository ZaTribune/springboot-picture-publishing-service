package zatribune.spring.pps.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {
    private Long id;

    @NotBlank
    @Size(min = 8,max = 255)// by default hibernate on the database creates this column with a length of 255
    private String username;
    private String password;
    private String passwordConfirm;
}
