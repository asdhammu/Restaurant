package my.restaurant.modal.forms;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserUpdateForm (
        @NotEmpty(message = "first name is required")
        @NotNull
        String firstName,
        @NotEmpty(message = "last name is required")
        @NotNull
        String lastName
) {
}
