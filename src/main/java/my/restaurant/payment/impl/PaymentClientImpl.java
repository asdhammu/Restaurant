package my.restaurant.payment.impl;

import jakarta.transaction.Transactional;
import my.restaurant.modal.forms.PaymentForm;
import my.restaurant.payment.PaymentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentClientImpl implements PaymentClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentClient.class);

    @Override
    public void processPayment(PaymentForm paymentForm) {
        LOGGER.info("Payment processed");

        // TODO: implement payment processor
    }
}
