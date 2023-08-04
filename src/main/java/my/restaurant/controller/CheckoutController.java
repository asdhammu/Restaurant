package my.restaurant.controller;

import jakarta.servlet.http.HttpSession;
import my.restaurant.dto.AddressDTO;
import my.restaurant.dto.OrderDTO;
import my.restaurant.dto.PaymentDTO;
import my.restaurant.modal.Selected;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.modal.forms.SelectAddressCheckoutForm;
import my.restaurant.modal.forms.SelectPaymentCheckoutForm;
import my.restaurant.service.*;
import my.restaurant.validation.BillingAddressGroup;
import my.restaurant.validation.PaymentGroup;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CheckoutController {

    private final AddressService addressService;
    private final PaymentService paymentService;
    private final AuthenticationFacade authenticationFacade;
    private final CartService cartService;
    private final StateService stateService;
    private final CountryService countryService;
    private final CheckoutService checkoutService;
    private final SmartValidator smartValidator;

    public CheckoutController(AddressService addressService, PaymentService paymentService, AuthenticationFacade authenticationFacade, CartService cartService, StateService stateService, CountryService countryService, CheckoutService checkoutService, SmartValidator smartValidator) {
        this.addressService = addressService;
        this.paymentService = paymentService;
        this.authenticationFacade = authenticationFacade;
        this.cartService = cartService;
        this.stateService = stateService;
        this.countryService = countryService;
        this.checkoutService = checkoutService;
        this.smartValidator = smartValidator;
    }

    @GetMapping("checkout")
    public String getCheckout(HttpSession httpSession, Model model) {
        ShoppingCart shoppingCart = this.cartService.getCart();
        CheckoutForm checkoutForm = new CheckoutForm();
        if (authenticationFacade.isLoggedIn()) {
            // For authenticated users fill checkout form if there are default payments and address
            Selected selected = selected(httpSession);
            PaymentDTO defaultPayment;
            if (selected.getPaymentId() != null) {
                defaultPayment = this.paymentService.getPayment(selected.getPaymentId());
            } else {
                defaultPayment = this.paymentService.getDefaultPayment();
            }

            AddressDTO defaultAddress;
            if (selected.getBillingAddressId() != null) {
                defaultAddress = this.addressService.getAddressById(selected.getBillingAddressId());
            } else {
                defaultAddress = this.addressService.getDefaultAddress();
            }

            if (defaultPayment != null) {
                checkoutForm.setCreditCard(defaultPayment.creditCard());
                checkoutForm.setCvv(defaultPayment.cvv());
                checkoutForm.setExpirationMonth(defaultPayment.expiryMonth());
                checkoutForm.setExpirationYear(defaultPayment.expiryYear());
                checkoutForm.setPersistedPayment(true);
                checkoutForm.setPaymentId(defaultPayment.id());
                selected.setPaymentId(defaultPayment.id());
            }
            if (defaultAddress != null) {
                checkoutForm.setFirstName(defaultAddress.firstName());
                checkoutForm.setLastName(defaultAddress.lastName());
                checkoutForm.setEmailId(defaultAddress.emailId());
                checkoutForm.setCountry(defaultAddress.country());
                checkoutForm.setStreetAddress1(defaultAddress.street1());
                checkoutForm.setStreetAddress2(defaultAddress.street2());
                checkoutForm.setCity(defaultAddress.city());
                checkoutForm.setPostalCode(defaultAddress.postalCode());
                checkoutForm.setState(defaultAddress.state());
                checkoutForm.setPhoneNumber(defaultAddress.phoneNumber());
                checkoutForm.setPersistedBillingAddress(true);
                checkoutForm.setAddressId(defaultAddress.id());
                selected.setBillingAddressId(defaultAddress.id());
            }
            httpSession.setAttribute("selected", selected);
            model.addAttribute("defaultPayment", defaultPayment);
            model.addAttribute("defaultAddresses", defaultAddress);
        }

        model.addAttribute("checkoutForm", checkoutForm);
        if (shoppingCart == null || shoppingCart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("items", shoppingCart.getCartItems());
        model.addAttribute("tax", shoppingCart.getTax());
        model.addAttribute("price", shoppingCart.getPrice());
        model.addAttribute("shipping", shoppingCart.getShipping());
        model.addAttribute("countries", this.countryService.getCountries());
        model.addAttribute("selectAddressFlag", false);
        model.addAttribute("selectPaymentFlag", false);
        return "checkout";
    }

    @PostMapping("checkout")
    public String processCheckout(@ModelAttribute("checkoutForm") CheckoutForm checkoutForm, HttpSession httpSession, BindingResult bindingResult,
                                  Model model) {
        ShoppingCart shoppingCart = this.cartService.getCart();
        if (shoppingCart == null || shoppingCart.isEmpty()) {
            return "redirect:/cart";
        }

        Selected selected = selected(httpSession);
        PaymentDTO defaultPayment;
        if (selected.getPaymentId() != null) {
            defaultPayment = this.paymentService.getPayment(selected.getPaymentId());
        } else {
            defaultPayment = this.paymentService.getDefaultPayment();
        }

        AddressDTO defaultAddress;
        if (selected.getBillingAddressId() != null) {
            defaultAddress = this.addressService.getAddressById(selected.getBillingAddressId());
        } else {
            defaultAddress = this.addressService.getDefaultAddress();
        }
        model.addAttribute("defaultPayment", defaultPayment);
        model.addAttribute("defaultAddresses", defaultAddress);
        model.addAttribute("countries", this.countryService.getCountries());
        if (checkoutForm.getCountry() != null) {
            model.addAttribute("states", this.stateService.getStatesByCountry(checkoutForm.getCountry()));
        }

        if (!checkoutForm.isPersistedPayment() && !checkoutForm.isPersistedBillingAddress()) {
            smartValidator.validate(checkoutForm, bindingResult, PaymentGroup.class, BillingAddressGroup.class);
        } else {
            if (!checkoutForm.isPersistedPayment()) {
                smartValidator.validate(checkoutForm, bindingResult, PaymentGroup.class);
            }
            if (!checkoutForm.isPersistedBillingAddress()) {
                smartValidator.validate(checkoutForm, bindingResult, BillingAddressGroup.class);
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("selectAddressFlag", false);
            model.addAttribute("selectPaymentFlag", false);
            model.addAttribute("items", shoppingCart.getCartItems());
            model.addAttribute("tax", shoppingCart.getTax());
            model.addAttribute("price", shoppingCart.getPrice());
            model.addAttribute("shipping", shoppingCart.getShipping());
            return "checkout";
        }

        OrderDTO orderDTO = this.checkoutService.processCheckout(checkoutForm);
        httpSession.removeAttribute("selected");
        model.addAttribute("order", orderDTO);
        return "redirect:order/confirmation?id=" + orderDTO.orderId() + "&emailId=" + checkoutForm.getEmailId();
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("selectAddress/checkout")
    public String selectAddressCheckout(@Param("addressId") Long addressId, HttpSession httpSession, Model model) {
        model.addAttribute("defaultPayment", this.paymentService.getDefaultPayment());
        model.addAttribute("addressList", this.addressService.getAddressesForUser());
        model.addAttribute("countries", this.countryService.getCountries());
        ShoppingCart shoppingCart = this.cartService.getCart();
        if (shoppingCart == null || shoppingCart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("items", shoppingCart.getCartItems());
        model.addAttribute("tax", shoppingCart.getTax());
        model.addAttribute("price", shoppingCart.getPrice());
        model.addAttribute("shipping", shoppingCart.getShipping());
        Selected selected = selected(httpSession);
        if (addressId != null) {
            selected.setBillingAddressId(addressId);
        }
        model.addAttribute("selectAddressCheckout", new SelectAddressCheckoutForm(selected.getBillingAddressId()));
        model.addAttribute("selectAddressFlag", true);
        model.addAttribute("selectPaymentFlag", false);
        return "checkout/selectAddress";
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("selectPayment/checkout")
    public String selectPaymentCheckout(@Param("paymentId") Long paymentId, HttpSession httpSession, Model model) {
        model.addAttribute("paymentList", this.paymentService.getPayments());
        model.addAttribute("defaultAddresses", this.addressService.getDefaultAddress());
        ShoppingCart shoppingCart = this.cartService.getCart();
        if (shoppingCart == null || shoppingCart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("items", shoppingCart.getCartItems());
        model.addAttribute("tax", shoppingCart.getTax());
        model.addAttribute("price", shoppingCart.getPrice());
        model.addAttribute("shipping", shoppingCart.getShipping());
        Selected selected = selected(httpSession);
        if (paymentId != null) {
            selected.setPaymentId(paymentId);
        }
        model.addAttribute("selectPaymentCheckout", new SelectPaymentCheckoutForm(selected.getPaymentId()));
        model.addAttribute("selectPaymentFlag", true);
        model.addAttribute("selectAddressFlag", false);
        return "checkout/selectPayment";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("selectAddress/checkout")
    public String selectAddressCheckout(@ModelAttribute("selectAddressCheckout") SelectAddressCheckoutForm addressCheckoutForm, HttpSession httpSession,
                                        BindingResult bindingResult, Model model) {

        Selected selected = selected(httpSession);
        selected.setBillingAddressId(addressCheckoutForm.selectedId());
        httpSession.setAttribute("selected", selected);
        return "redirect:/checkout";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("selectPayment/checkout")
    public String selectPaymentCheckout(@ModelAttribute("selectAddressCheckout") SelectPaymentCheckoutForm paymentCheckoutForm, HttpSession httpSession,
                                        BindingResult bindingResult, Model model) {

        Selected selected = selected(httpSession);
        selected.setPaymentId(paymentCheckoutForm.paymentId());
        httpSession.setAttribute("selected", selected);
        return "redirect:/checkout";
    }


    private Selected selected(HttpSession httpSession) {
        Selected selected = (Selected) httpSession.getAttribute("selected");
        return selected != null ? selected : new Selected();
    }

}
