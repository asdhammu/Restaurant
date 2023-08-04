package my.restaurant.dto;

import org.springframework.validation.ObjectError;

import java.util.List;

public record AddressModalDTO(AddressDTO address, List<ObjectError> errorList) {
}
