package my.restaurant.config.security;

import jakarta.servlet.http.HttpSession;
import my.restaurant.dto.MyUserDetails;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);
    private final HttpSession httpSession;
    private final CartService cartService;

    public AuthenticationEventListener(HttpSession httpSession, CartService cartService) {
        this.httpSession = httpSession;
        this.cartService = cartService;
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
        logger.debug("init cart after successful login");
        MyUserDetails myUserDetails = (MyUserDetails) authenticationSuccessEvent.getAuthentication().getPrincipal();
        this.cartService.initCart(myUserDetails.getUsername());
        ShoppingCart shoppingCart = (ShoppingCart) httpSession.getAttribute("cart");
        if (shoppingCart != null) {
            this.cartService.mergeShoppingCart(shoppingCart, myUserDetails.getUsername());
            httpSession.removeAttribute("cart");
            logger.debug("Shopping cart merged");
        }
    }
}
