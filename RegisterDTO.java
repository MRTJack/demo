package uz.pdp.app_auditing_project.hr_management.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;



@Data
public class RegisterDTO {
    @Size(min = 3, max = 20)
    private String first_name;

    @Length(min = 3, max = 20)
    private String last_name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

}
