package zatribune.spring.pps.domain;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


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
