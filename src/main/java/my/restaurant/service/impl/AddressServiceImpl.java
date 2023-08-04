package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.AddressDTO;
import my.restaurant.entity.*;
import my.restaurant.exceptions.AddressNotFoundException;
import my.restaurant.exceptions.CountryNotFoundException;
import my.restaurant.exceptions.StateNotFoundException;
import my.restaurant.modal.AddressType;
import my.restaurant.modal.forms.AddressForm;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.EditAddressForm;
import my.restaurant.repository.*;
import my.restaurant.service.AddressService;
import my.restaurant.service.AuthenticationFacade;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final UserAddressRepository userAddressRepository;
    private final AddressRepository addressRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public AddressServiceImpl(UserAddressRepository userAddressRepository, AddressRepository addressRepository, StateRepository stateRepository, CountryRepository countryRepository, AuthenticationFacade authenticationFacade, UserRepository userRepository) {
        this.userAddressRepository = userAddressRepository;
        this.addressRepository = addressRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressDTO> getAddressesForUser() {
        List<UserAddress> addresses = this.userAddressRepository.getAddressByUserUsername(this.authenticationFacade.getUsername());
        return addresses.stream().map(address -> new AddressDTO(address.getFirstName(), address.getLastName(), address.getEmailId(), address.getAddressId(), address.getStreetAddress1(), address.getStreetAddress2(), address.getCountry().getName(), address.getState().getName(),
                address.getPostalCode(), address.getCity(), address.isMyDefault(), address.getAddressType().toString(), address.getPhoneNumber())).toList();
    }

    @Override
    public AddressDTO addAddress(AddressForm addressForm) {
        User user = verifyUser(this.authenticationFacade.getUsername());
        Country country = verifyCountry(addressForm.getCountry());
        State state = verifyState(addressForm.getState());
        UserAddress address = new UserAddress();
        UserAddress savedAddress = persistAddress(addressForm, country, state, address, user);
        return new AddressDTO(savedAddress.getFirstName(), savedAddress.getLastName(), savedAddress.getEmailId(), savedAddress.getAddressId(), savedAddress.getStreetAddress1(), savedAddress.getStreetAddress2(), savedAddress.getCountry().getName(),
                savedAddress.getState().getName(), savedAddress.getPostalCode(), savedAddress.getCity(), savedAddress.isMyDefault(), savedAddress.getAddressType().toString(),
                savedAddress.getPhoneNumber());
    }

    @Override
    public AddressDTO addAddress(CheckoutForm checkoutForm) {
        Country country = verifyCountry(checkoutForm.getCountry());
        State state = verifyState(checkoutForm.getState());
        Address address  = new Address();
        address.setFirstName(checkoutForm.getFirstName());
        address.setLastName(checkoutForm.getLastName());
        address.setStreetAddress1(checkoutForm.getStreetAddress1());
        address.setStreetAddress2(checkoutForm.getStreetAddress2());
        address.setCountry(country);
        address.setState(state);
        address.setPostalCode(checkoutForm.getPostalCode());
        address.setAddressType(AddressType.BILLING);
        address.setPhoneNumber(checkoutForm.getPhoneNumber());
        address.setCity(checkoutForm.getCity());
        address.setEmailId(checkoutForm.getEmailId());
        Address persistedAddress = this.addressRepository.save(address);
        return new AddressDTO(persistedAddress.getAddressId());

    }

    @Override
    public AddressDTO editAddress(EditAddressForm addressForm) {
        User user = verifyUser(this.authenticationFacade.getUsername());
        UserAddress address = verifyAddress(addressForm.getAddressId());
        Country country = verifyCountry(addressForm.getCountry());
        State state = verifyState(addressForm.getState());
        UserAddress savedAddress = persistAddress(addressForm, country, state, address, user);
        return new AddressDTO(savedAddress.getFirstName(), savedAddress.getLastName(), savedAddress.getEmailId(), savedAddress.getAddressId(), savedAddress.getStreetAddress1(), savedAddress.getStreetAddress2(), savedAddress.getCountry().getName(),
                savedAddress.getState().getName(), savedAddress.getPostalCode(), savedAddress.getCity(), savedAddress.isMyDefault(), savedAddress.getAddressType().toString(),
                savedAddress.getPhoneNumber());
    }

    private User verifyUser(String username) {

        Optional<User> userOptional = this.userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new UsernameNotFoundException(String.format("User not found %s", username));

        return userOptional.get();

    }

    private UserAddress verifyAddress(long id) {
        Optional<UserAddress> addressOptional = this.userAddressRepository.findById(id);

        if (addressOptional.isEmpty())
            throw new AddressNotFoundException(String.format("No address found for %s", id));
        return addressOptional.get();
    }

    @Override
    public void deleteAddress(long id) {
        UserAddress address = verifyAddress(id);
        this.userAddressRepository.delete(address);
    }

    @Override
    public AddressDTO getAddressById(long id) {

        UserAddress address = this.userAddressRepository.getAddressByAddressIdAndUserUsername(id, this.authenticationFacade.getUsername());

        if (address == null) throw new AddressNotFoundException(String.format("No address found for %s", id));

        return new AddressDTO(address.getFirstName(), address.getLastName(), address.getEmailId(), address.getAddressId(), address.getStreetAddress1(), address.getStreetAddress2(), address.getCountry().getName(), address.getState().getName(),
                address.getPostalCode(), address.getCity(), address.isMyDefault(), address.getAddressType().toString(), address.getPhoneNumber());
    }

    @Override
    public AddressDTO getDefaultAddress() {
        String username = this.authenticationFacade.getUsername();
        UserAddress address = this.userAddressRepository.getAddressByMyDefaultIsTrueAndUserUsername(username);
        if (address == null)
            return null;

        return new AddressDTO(address.getFirstName(), address.getLastName(), address.getEmailId(), address.getAddressId(), address.getStreetAddress1(), address.getStreetAddress2(), address.getCountry().getName(), address.getState().getName(),
                address.getPostalCode(), address.getCity(), address.isMyDefault(), address.getAddressType().toString(), address.getPhoneNumber());
    }

    private UserAddress persistAddress(AddressForm addressForm, Country country, State state, UserAddress address, User user) {
        address.setFirstName(addressForm.getFirstName());
        address.setLastName(addressForm.getLastName());
        address.setStreetAddress1(addressForm.getStreetAddress1());
        address.setStreetAddress2(addressForm.getStreetAddress2());
        address.setCountry(country);
        address.setState(state);
        address.setPostalCode(addressForm.getPostalCode());
        address.setAddressType(AddressType.BILLING);
        address.setPhoneNumber(addressForm.getPhoneNumber());
        address.setCity(addressForm.getCity());
        address.setEmailId(addressForm.getEmailId());
        address.setUser(user);
        UserAddress defaultAddress = this.userAddressRepository.getAddressByMyDefaultIsTrueAndUserUsername(user.getUsername());
        if (defaultAddress == null) {
            address.setMyDefault(true);
        } else if (addressForm.isMyDefault()) {
            defaultAddress.setMyDefault(false);
            this.userAddressRepository.save(defaultAddress);
            address.setMyDefault(true);
        }

        return this.userAddressRepository.save(address);
    }

    private State verifyState(String state) {
        Optional<State> stateOptional = this.stateRepository.findStateByName(state);

        if (stateOptional.isEmpty())
            throw new StateNotFoundException(String.format("No state for %s", state));
        return stateOptional.get();
    }

    private Country verifyCountry(String country) {
        Optional<Country> countryOptional = this.countryRepository.findCountryByName(country);

        if (countryOptional.isEmpty())
            throw new CountryNotFoundException(String.format("no country for %s", country));
        return countryOptional.get();
    }
}
