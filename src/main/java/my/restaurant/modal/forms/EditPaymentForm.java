package my.restaurant.modal.forms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.validation.PaymentGroup;

@Getter
@Setter
public class EditPaymentForm extends PaymentForm {
    @NotNull(message = "payment id is required", groups = PaymentGroup.class)
    private Long id;
}
