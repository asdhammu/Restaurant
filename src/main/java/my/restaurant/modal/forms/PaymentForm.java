package my.restaurant.modal.forms;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.validation.PaymentGroup;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class PaymentForm {

    @NotEmpty(message = "credit card is required", groups = {PaymentGroup.class})
    @Length(min = 16, max = 19, message = "Not valid credit card number", groups = {PaymentGroup.class})
    @Pattern(regexp = "\\d+", message = "Only numbers are allowed", groups = {PaymentGroup.class})
    private String creditCard;

    @NotEmpty(message = "cvv is required", groups = {PaymentGroup.class})
    @Length(min = 3, max = 4, message = "Invalid cvv", groups = {PaymentGroup.class})
    private String cvv;

    @NotNull(message = "expiration year is required", groups = {PaymentGroup.class})
    @Max(value = 9999, message = "Must be 4 digit year", groups = {PaymentGroup.class})
    @Min(value = 2000, message = "Must be 4 digit year", groups = {PaymentGroup.class})
    private Integer expirationYear;

    @NotNull(message = "expiration month is required", groups = {PaymentGroup.class})
    @Min(value = 1, message = "Not Valid Month", groups = {PaymentGroup.class})
    @Max(value = 12, message = "Not Valid Month", groups = {PaymentGroup.class})
    private Integer expirationMonth;

    @NotEmpty(message = "full name is required", groups = {PaymentGroup.class})
    @Length(max = 100, groups = {PaymentGroup.class})
    private String fullName;

    private boolean myDefault;
}
