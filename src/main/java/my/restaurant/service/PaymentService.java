package my.restaurant.service;

import my.restaurant.dto.PaymentDTO;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.EditPaymentForm;
import my.restaurant.modal.forms.PaymentForm;

import java.util.List;

public interface PaymentService {

    PaymentDTO addPayment(PaymentForm paymentForm);
    PaymentDTO addPayment(CheckoutForm checkoutForm);
    void editPayment(EditPaymentForm editPaymentForm);
    List<PaymentDTO> getPayments();
    PaymentDTO getDefaultPayment();
    PaymentDTO getPayment(long paymentId);
    void deletePayment(long paymentId);
    void processPayment(PaymentForm paymentForm);

}
