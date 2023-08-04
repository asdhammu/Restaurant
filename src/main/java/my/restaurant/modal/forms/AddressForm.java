package my.restaurant.modal.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AddressForm {

    @NotEmpty(message = "first name is required")
    @Length(max = 100, min = 1)
    private String firstName;

    @Length(min = 1, max = 100)
    @NotEmpty(message = "last name is required")
    private String lastName;

    @Length(max = 100)
    @NotEmpty(message = "email is required")
    @Email
    private String emailId;

    @NotEmpty(message = "street address is required")
    @Length(max = 200)
    private String streetAddress1;

    @Length(max = 200)
    private String streetAddress2;

    @NotEmpty(message = "city is required")
    @Length(max = 100)
    private String city;

    @NotEmpty(message = "state is required")
    @Length(max = 100)
    private String state;

    @NotEmpty(message = "postalCode is required")
    @Length(max = 20)
    private String postalCode;

    @NotEmpty(message = "country is required")
    @Length(max = 100)
    private String country;

    @NotEmpty(message = "phone number is required")
    @Length(max = 20)
    private String phoneNumber;
    private boolean myDefault;
}
