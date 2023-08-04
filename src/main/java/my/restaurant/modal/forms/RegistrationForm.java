package my.restaurant.modal.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

    @NotEmpty(message = "first name is required")
    private String firstName;
    @NotEmpty(message = "last name is required")
    private String lastName;
    @NotEmpty(message = "email id is required")
    @Email
    private String emailId;
    @NotEmpty(message = "password is required")
    private String password;
}
