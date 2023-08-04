package my.restaurant.service;

import my.restaurant.dto.OrderDTO;
import my.restaurant.modal.forms.CheckoutForm;

public interface CheckoutService {
    OrderDTO processCheckout(CheckoutForm checkoutForm);
}
