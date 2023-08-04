package my.restaurant.utils;

import my.restaurant.dto.AddressDTO;
import my.restaurant.dto.CurrencyDTO;
import my.restaurant.dto.PaymentDTO;
import my.restaurant.modal.forms.EditAddressForm;
import my.restaurant.modal.forms.EditPaymentForm;

public class RestaurantUtils {

    public static CurrencyDTO getDefaultCurrency() {
        return new CurrencyDTO("USD", "$");
    }

    public static EditAddressForm mapAddressDTOToEditAddressForm(AddressDTO addressDTO) {
        EditAddressForm editAddressForm = new EditAddressForm();
        editAddressForm.setFirstName(addressDTO.firstName());
        editAddressForm.setLastName(addressDTO.lastName());
        editAddressForm.setPhoneNumber(addressDTO.phoneNumber());
        editAddressForm.setAddressId(addressDTO.id());
        editAddressForm.setStreetAddress1(addressDTO.street1());
        editAddressForm.setStreetAddress2(addressDTO.street2());
        editAddressForm.setCountry(addressDTO.country());
        editAddressForm.setState(addressDTO.state());
        editAddressForm.setPostalCode(addressDTO.postalCode());
        editAddressForm.setMyDefault(addressDTO.myDefault());
        editAddressForm.setCity(addressDTO.city());
        editAddressForm.setEmailId(addressDTO.emailId());
        return editAddressForm;
    }

    public static EditPaymentForm mapPaymentDTOToEditPaymentForm(PaymentDTO paymentDTO) {
        EditPaymentForm editPaymentForm = new EditPaymentForm();
        editPaymentForm.setId(paymentDTO.id());
        editPaymentForm.setCvv(paymentDTO.cvv());
        editPaymentForm.setCreditCard(paymentDTO.creditCard());
        editPaymentForm.setExpirationMonth(paymentDTO.expiryMonth());
        editPaymentForm.setExpirationYear(paymentDTO.expiryYear());
        editPaymentForm.setFullName(paymentDTO.fullName());
        editPaymentForm.setMyDefault(paymentDTO.myDefault());
        return editPaymentForm;
    }

}
