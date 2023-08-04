package my.restaurant.payment;

import my.restaurant.modal.forms.PaymentForm;

public interface PaymentClient {
    void processPayment(PaymentForm paymentForm);
}
