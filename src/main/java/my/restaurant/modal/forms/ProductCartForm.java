package my.restaurant.modal.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductCartForm {
    @NotNull(message = "product id is required")
    private UUID productId;
    private int quantity = 1;
}
