package my.restaurant.modal.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderLookupForm {
    @NotNull
    @NotEmpty(message = "email is required")
    private String emailId;
    @NotEmpty(message = "order is required")
    @NotNull
    private UUID orderNumber;
}
