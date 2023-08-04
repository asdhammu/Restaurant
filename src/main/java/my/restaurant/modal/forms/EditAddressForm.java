package my.restaurant.modal.forms;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAddressForm extends AddressForm {
    @NotNull(message = "address Id is required")
    private Long addressId;
}
