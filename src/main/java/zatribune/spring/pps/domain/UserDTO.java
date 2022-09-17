package zatribune.spring.pps.domain;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {
    private Long id;

    @Email
    @NotBlank
    @Size(min = 8,max = 255)// by default hibernate on the database creates this column with a length of 255
    private String username;
    @Size(min = 8,max = 255)
    private String password;
    private String passwordConfirm;
}
