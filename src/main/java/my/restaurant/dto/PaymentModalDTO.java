package my.restaurant.dto;

import org.springframework.validation.ObjectError;

import java.util.List;

public record PaymentModalDTO(PaymentDTO payment, List<ObjectError> errorList) {
}
