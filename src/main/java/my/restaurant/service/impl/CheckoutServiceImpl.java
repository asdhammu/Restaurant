package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.AddressDTO;
import my.restaurant.dto.OrderDTO;
import my.restaurant.dto.PaymentDTO;
import my.restaurant.entity.*;
import my.restaurant.modal.EmailType;
import my.restaurant.modal.OrderStatus;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.repository.OrderRepository;
import my.restaurant.repository.UserRepository;
import my.restaurant.service.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {
    private final PaymentService paymentService;
    private final AddressService addressService;
    private final CartService cartService;
    private final EmailService emailService;
    private final OrderService orderService;

    public CheckoutServiceImpl(PaymentService paymentService, AddressService addressService, CartService cartService, EmailService emailService, OrderService orderService) {
        this.paymentService = paymentService;
        this.addressService = addressService;
        this.cartService = cartService;
        this.emailService = emailService;
        this.orderService = orderService;
    }

    @Override
    public OrderDTO processCheckout(CheckoutForm checkoutForm) {
        ShoppingCart shoppingCart = this.cartService.getCart();

        // check if the payment is default, then don't save in payment
        PaymentDTO paymentDTO;
        if (!checkoutForm.isPersistedPayment()) {
            paymentDTO = this.paymentService.addPayment(checkoutForm);
        } else {
            paymentDTO = this.paymentService.getPayment(checkoutForm.getPaymentId());
            CheckoutForm checkoutPaymentForm = new CheckoutForm();
            checkoutPaymentForm.setCreditCard(paymentDTO.creditCard());
            checkoutPaymentForm.setCvv(paymentDTO.cvv());
            checkoutPaymentForm.setFullName(paymentDTO.fullName());
            checkoutPaymentForm.setExpirationYear(paymentDTO.expiryYear());
            checkoutPaymentForm.setExpirationMonth(paymentDTO.expiryMonth());
            paymentDTO = this.paymentService.addPayment(checkoutPaymentForm);
        }

        // check if the address is default, then don't persist
        AddressDTO addressDTO;
        if (!checkoutForm.isPersistedBillingAddress()) {
            addressDTO = this.addressService.addAddress(checkoutForm);
        } else {
            addressDTO = this.addressService.getAddressById(checkoutForm.getAddressId());
            CheckoutForm checkoutF = new CheckoutForm();
            checkoutF.setFirstName(addressDTO.firstName());
            checkoutF.setLastName(addressDTO.lastName());
            checkoutF.setEmailId(addressDTO.emailId());
            checkoutF.setStreetAddress1(addressDTO.street1());
            checkoutF.setStreetAddress2(addressDTO.street2());
            checkoutF.setState(addressDTO.state());
            checkoutF.setCity(addressDTO.city());
            checkoutF.setPostalCode(addressDTO.postalCode());
            checkoutF.setCountry(addressDTO.country());
            checkoutF.setPhoneNumber(addressDTO.phoneNumber());
            addressDTO = this.addressService.addAddress(checkoutF);
        }

        // process payment
        this.paymentService.processPayment(checkoutForm);
        OrderDTO orderDTO = this.orderService.addOrder(shoppingCart, addressDTO, paymentDTO);
        this.cartService.clearShoppingCart();
        this.emailService.sendEmail(EmailType.ORDER_CONFIRMATION, checkoutForm.getEmailId(), orderDTO);
        return new OrderDTO(orderDTO.orderId());
    }
}
