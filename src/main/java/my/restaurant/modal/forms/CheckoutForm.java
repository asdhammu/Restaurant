package my.restaurant.modal.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import my.restaurant.validation.BillingAddressGroup;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CheckoutForm extends PaymentForm {

    @NotEmpty(message = "first name is required", groups = {BillingAddressGroup.class})
    @Length(max = 100, min = 1, groups = {BillingAddressGroup.class})
    private String firstName;

    @Length(min = 1, max = 100, groups = {BillingAddressGroup.class})
    @NotEmpty(message = "last name is required", groups = {BillingAddressGroup.class})
    private String lastName;

    @Length(max = 100, groups = {BillingAddressGroup.class})
    @NotEmpty(message = "email is required", groups = {BillingAddressGroup.class})
    @Email
    private String emailId;

    @NotEmpty(message = "street address is required", groups = {BillingAddressGroup.class})
    @Length(max = 200, groups = {BillingAddressGroup.class})
    private String streetAddress1;

    @Length(max = 200, groups = {BillingAddressGroup.class})
    private String streetAddress2;

    @NotEmpty(message = "state is required", groups = {BillingAddressGroup.class})
    @Length(max = 100)
    private String state;

    @NotEmpty(message = "city is required", groups = {BillingAddressGroup.class})
    @Length(max = 100, groups = {BillingAddressGroup.class})
    private String city;

    @NotEmpty(message = "postalCode is required", groups = {BillingAddressGroup.class})
    @Length(max = 20, groups = {BillingAddressGroup.class})
    private String postalCode;

    @NotEmpty(message = "country is required", groups = {BillingAddressGroup.class})
    @Length(max = 100)
    private String country;

    @NotEmpty(message = "phone number is required", groups = {BillingAddressGroup.class})
    @Length(max = 20, groups = {BillingAddressGroup.class})
    private String phoneNumber;

    private boolean persistedBillingAddress;

    private boolean persistedPayment;

    private long paymentId;
    private long addressId;


}
