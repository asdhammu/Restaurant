package my.restaurant.controller;

import my.restaurant.dto.OrderDTO;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.modal.forms.CheckoutForm;
import my.restaurant.security.WithMockCustomUser;
import my.restaurant.service.*;
import my.restaurant.util.UrlConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CheckoutController.class)
@WithMockCustomUser
public class CheckoutControllerTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    AddressService addressService;
    @MockBean
    PaymentService paymentService;
    @MockBean
    AuthenticationFacade authenticationFacade;
    @MockBean
    CartService cartService;
    @MockBean
    StateService stateService;
    @MockBean
    CountryService countryService;
    @MockBean
    CheckoutService checkoutService;
    private MockMvc mockMvc;
    private final UUID orderId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(new ProductDTO(UUID.randomUUID(), new PriceDTO(12)), 1);
        Mockito.when(cartService.getCart()).thenReturn(shoppingCart);
        Mockito.when(checkoutService.processCheckout(any())).thenReturn(new OrderDTO(orderId));
    }


    @Test
    public void getCheckout_returnsError() throws Exception {
        this.mockMvc.perform(get(UrlConstants.Checkout)
                        .with(csrf())).andExpect(model()
                        .attributeHasNoErrors("checkoutForm"))
                .andExpect(view().name("checkout"));
    }

    @Test
    public void postCheckout_returnsError() throws Exception {
        CheckoutForm checkoutForm = new CheckoutForm();
        this.mockMvc.perform(post(UrlConstants.Checkout).
                        flashAttr("checkoutForm", checkoutForm).with(csrf())).andExpect(model()
                        .attributeHasFieldErrors("checkoutForm",
                                "creditCard", "cvv", "expirationYear", "expirationMonth", "fullName"))
                .andExpect(view().name("checkout"));
    }

    @Test
    public void postCheckoutWithDefaultPaymentAndAddress_returnsError() throws Exception {
        CheckoutForm checkoutForm = new CheckoutForm();
        checkoutForm.setPersistedBillingAddress(true);
        checkoutForm.setPersistedPayment(true);
        this.mockMvc.perform(post(UrlConstants.Checkout).
                        flashAttr("checkoutForm", checkoutForm).with(csrf()))
                .andExpect(model().errorCount(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:order/confirmation?id=" + orderId + "&emailId=null"));
    }

}
