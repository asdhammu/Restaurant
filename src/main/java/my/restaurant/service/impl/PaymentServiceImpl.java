package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.PaymentDTO;
import my.restaurant.entity.PaymentMethod;
import my.restaurant.entity.User;
import my.restaurant.entity.UserPaymentMethod;
import my.restaurant.exceptions.PaymentNotFoundException;
import my.restaurant.modal.PaymentMethodType;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.EditPaymentForm;
import my.restaurant.modal.forms.PaymentForm;
import my.restaurant.payment.PaymentClient;
import my.restaurant.repository.PaymentRepository;
import my.restaurant.repository.UserPaymentMethodRepository;
import my.restaurant.repository.UserRepository;
import my.restaurant.service.AuthenticationFacade;
import my.restaurant.service.PaymentService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final UserPaymentMethodRepository userPaymentMethodRepository;
    private final PaymentRepository paymentRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;
    private final PaymentClient paymentClient;

    public PaymentServiceImpl(UserPaymentMethodRepository userPaymentMethodRepository, PaymentRepository paymentRepository, AuthenticationFacade authenticationFacade, UserRepository userRepository, PaymentClient paymentClient) {
        this.userPaymentMethodRepository = userPaymentMethodRepository;
        this.paymentRepository = paymentRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
        this.paymentClient = paymentClient;
    }

    @Override
    public PaymentDTO addPayment(PaymentForm paymentForm) {
        UserPaymentMethod paymentMethod = new UserPaymentMethod();

        User user = verifyUser(this.authenticationFacade.getUsername());
        paymentMethod.setUser(user);
        // check for my default payments, if none mark this one
        UserPaymentMethod defaultPayment = this.userPaymentMethodRepository.findByMyDefaultIsTrueAndUserUsername(user.getUsername());
        if (defaultPayment == null) {
            paymentMethod.setMyDefault(true);
        } else if (paymentForm.isMyDefault()) {
            paymentMethod.setMyDefault(true);
            // mark other one false
            defaultPayment.setMyDefault(false);
            this.userPaymentMethodRepository.save(defaultPayment);
        }
        paymentMethod.setCreditCardNumber(paymentForm.getCreditCard());
        paymentMethod.setPaymentMethodType(PaymentMethodType.VISA);
        paymentMethod.setCvv(paymentForm.getCvv());
        paymentMethod.setExpiryMonth(paymentForm.getExpirationMonth());
        paymentMethod.setExpiryYear(paymentForm.getExpirationYear());
        paymentMethod.setFullName(paymentForm.getFullName());
        UserPaymentMethod persistedPayment = this.userPaymentMethodRepository.save(paymentMethod);
        return new PaymentDTO(persistedPayment.getPaymentMethodId());
    }


    @Override
    public PaymentDTO addPayment(CheckoutForm checkoutForm) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCreditCardNumber(checkoutForm.getCreditCard());
        paymentMethod.setPaymentMethodType(PaymentMethodType.VISA);
        paymentMethod.setCvv(checkoutForm.getCvv());
        paymentMethod.setExpiryMonth(checkoutForm.getExpirationMonth());
        paymentMethod.setExpiryYear(checkoutForm.getExpirationYear());
        paymentMethod.setFullName(checkoutForm.getFullName());
        PaymentMethod persistedPayment = this.paymentRepository.save(paymentMethod);
        return new PaymentDTO(persistedPayment.getPaymentMethodId());
    }

    @Override
    public void editPayment(EditPaymentForm editPaymentForm) {
        UserPaymentMethod paymentMethod = verifyPayment(editPaymentForm.getId());
        User user = verifyUser(this.authenticationFacade.getUsername());
        paymentMethod.setUser(user);
        paymentMethod.setCreditCardNumber(editPaymentForm.getCreditCard());
        paymentMethod.setPaymentMethodType(PaymentMethodType.VISA);
        paymentMethod.setCvv(editPaymentForm.getCvv());
        paymentMethod.setExpiryMonth(editPaymentForm.getExpirationMonth());
        paymentMethod.setExpiryYear(editPaymentForm.getExpirationYear());
        paymentMethod.setFullName(editPaymentForm.getFullName());

        UserPaymentMethod defaultPayment = this.userPaymentMethodRepository.findByMyDefaultIsTrueAndUserUsername(user.getUsername());
        if (defaultPayment == null) {
            paymentMethod.setMyDefault(true);
        } else if (editPaymentForm.isMyDefault()) {
            paymentMethod.setMyDefault(true);
            // mark other one false
            defaultPayment.setMyDefault(false);
            this.userPaymentMethodRepository.save(defaultPayment);
        }

        this.userPaymentMethodRepository.save(paymentMethod);

    }

    @Override
    public List<PaymentDTO> getPayments() {
        String username = this.authenticationFacade.getUsername();
        List<UserPaymentMethod> payments = this.userPaymentMethodRepository.findByUserUsername(username);
        return payments.stream().map(sp ->
                        new PaymentDTO(sp.getPaymentMethodId(), sp.getCreditCardNumber(), sp.getCvv(), sp.getFullName(), sp.getExpiryMonth(), sp.getExpiryYear(), sp.isMyDefault()))
                .toList();
    }

    @Override
    public PaymentDTO getDefaultPayment() {
        String username = this.authenticationFacade.getUsername();
        UserPaymentMethod sp = this.userPaymentMethodRepository.findByMyDefaultIsTrueAndUserUsername(username);
        if (sp == null) return null;
        return new PaymentDTO(sp.getPaymentMethodId(), sp.getCreditCardNumber(), sp.getCvv(), sp.getFullName(), sp.getExpiryMonth(), sp.getExpiryYear(), sp.isMyDefault());
    }

    @Override
    public PaymentDTO getPayment(long paymentId) {
        UserPaymentMethod sp = verifyPayment(paymentId);
        return new PaymentDTO(sp.getPaymentMethodId(), sp.getCreditCardNumber(), sp.getCvv(), sp.getFullName(), sp.getExpiryMonth(), sp.getExpiryYear(), sp.isMyDefault());
    }

    @Override
    public void deletePayment(long paymentId) {
        UserPaymentMethod sp = verifyPayment(paymentId, this.authenticationFacade.getUsername());
        this.userPaymentMethodRepository.delete(sp);
    }

    @Override
    public void processPayment(PaymentForm paymentForm) {
        this.paymentClient.processPayment(paymentForm);
    }

    private UserPaymentMethod verifyPayment(long id, String username) {
        Optional<UserPaymentMethod> optionalPaymentMethod = this.userPaymentMethodRepository.findByPaymentMethodIdAndUserUsername(id, username);
        if (optionalPaymentMethod.isEmpty())
            throw new PaymentNotFoundException(String.format("Not payment for %s and user %s", id, username));

        return optionalPaymentMethod.get();
    }

    private UserPaymentMethod verifyPayment(long id) {
        Optional<UserPaymentMethod> optionalPaymentMethod = this.userPaymentMethodRepository.findById(id);
        if (optionalPaymentMethod.isEmpty())
            throw new PaymentNotFoundException(String.format("Not payment for %s", id));

        return optionalPaymentMethod.get();
    }

    private User verifyUser(String username) {
        Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new UsernameNotFoundException(String.format("User not found %s", username));

        return userOptional.get();

    }
}
