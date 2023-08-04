package my.restaurant.service;

import my.restaurant.dto.AddressDTO;
import my.restaurant.modal.forms.AddressForm;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.EditAddressForm;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddressesForUser();

    AddressDTO addAddress(AddressForm addressForm);

    AddressDTO addAddress(CheckoutForm checkoutForm);

    AddressDTO editAddress(EditAddressForm addressForm);

    void deleteAddress(long id);

    AddressDTO getAddressById(long id);

    AddressDTO getDefaultAddress();

}
