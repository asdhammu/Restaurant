package my.restaurant.modal.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotEmpty(message = "email id is required")
    @NotNull
    private String emailId;

    @NotEmpty(message = "password is required")
    @NotNull
    private String password;
}
