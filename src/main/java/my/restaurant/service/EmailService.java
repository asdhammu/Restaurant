package my.restaurant.service;

import my.restaurant.modal.EmailType;

public interface EmailService {
    void sendEmail(EmailType emailType, String emailId);
    void sendEmail(EmailType emailType, String emailId, Object payload);
}
