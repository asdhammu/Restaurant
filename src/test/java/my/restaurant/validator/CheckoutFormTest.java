package my.restaurant.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.PaymentForm;
import my.restaurant.validation.BillingAddressGroup;
import my.restaurant.validation.PaymentGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CheckoutFormTest {

    private Validator validator;
    private CheckoutForm checkoutForm;
    private PaymentForm paymentForm;

    @BeforeEach
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        checkoutForm = new CheckoutForm();
        paymentForm = new PaymentForm();
    }

    @Test
    public void skipValidationForPersistPaymentAndAddress() {


        checkoutForm.setPersistedPayment(true);
        checkoutForm.setPersistedBillingAddress(true);
        Set<ConstraintViolation<CheckoutForm>> violations = validator.validate(checkoutForm, BillingAddressGroup.class, PaymentGroup.class);
        assertEquals(14, violations.size());
    }

    @Test
    public void skipValidationForPersistPayment() {
        paymentForm.setMyDefault(true);
        Set<ConstraintViolation<PaymentForm>> violations = validator.validate(paymentForm, PaymentGroup.class);
        assertEquals(5, violations.size());
    }

}
