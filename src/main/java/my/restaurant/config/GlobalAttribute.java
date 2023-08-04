package my.restaurant.config;

import my.restaurant.service.CartService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAttribute {

    private final CartService cartService;

    private GlobalAttribute(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartCount")
    public int getCartCount() {
        return this.cartService.getCart().getCount();
    }


    @ModelAttribute("theme")
    public String themeAttribute() {
        return "light";
    }


}
