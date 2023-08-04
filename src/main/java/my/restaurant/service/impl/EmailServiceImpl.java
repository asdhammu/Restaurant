package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.modal.EmailType;
import my.restaurant.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Override
    public void sendEmail(EmailType emailType, String emailId) {
        LOGGER.info("Email sent");
    }

    @Override
    public void sendEmail(EmailType emailType, String emailId, Object payload) {
        LOGGER.info("Email sent with payload");
    }
}
