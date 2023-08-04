package my.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.modal.forms.PaymentForm;
import my.restaurant.security.WithMockCustomUser;
import my.restaurant.service.CartService;
import my.restaurant.service.PaymentService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PaymentController.class)
@WithMockCustomUser
public class PaymentControllerTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    PaymentService addressService;

    @MockBean
    CartService cartService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        Mockito.when(cartService.getCart()).thenReturn(new ShoppingCart());

    }

    @Test
    public void addPayment_returnsError() throws Exception {
        PaymentForm paymentForm = new PaymentForm();
        this.mockMvc.perform(post(UrlConstants.AddPayment).
                        flashAttr("paymentForm", paymentForm).with(csrf()))
                .andExpect(model()
                        .attributeHasFieldErrors("paymentForm",
                                "creditCard", "cvv", "expirationYear", "expirationMonth", "fullName"))
                .andExpect(view().name(UrlConstants.AddPaymentView));
    }

    @Test
    public void addPayment_successful() throws Exception {
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setFullName("full name");
        paymentForm.setCreditCard("1234123234535454");
        paymentForm.setExpirationMonth(12);
        paymentForm.setExpirationYear(2004);
        paymentForm.setCvv("123");
        paymentForm.setMyDefault(true);

        ResultActions resultActions = this.mockMvc.perform(post(UrlConstants.AddPayment)
                .flashAttr("paymentForm", paymentForm).with(csrf())
        );

        resultActions.andExpect(model().errorCount(0))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:" + UrlConstants.ListPayments));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
