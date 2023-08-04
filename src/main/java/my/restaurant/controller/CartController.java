package my.restaurant.controller;

import jakarta.validation.Valid;
import my.restaurant.dto.ProductDTO;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.modal.forms.ProductCartForm;
import my.restaurant.service.CartService;
import my.restaurant.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class CartController {
    private final ProductService productService;

    private final CartService cartService;

    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("cart")
    public String cartPage(Model model) {
        ShoppingCart shoppingCart = this.cartService.getCart();
        model.addAttribute("items", shoppingCart.getCartItems());
        model.addAttribute("price", shoppingCart.getPrice());
        model.addAttribute("currency", shoppingCart.getCurrency());
        model.addAttribute("tax", shoppingCart.getTax());
        model.addAttribute("productCartForm", new ProductCartForm());
        return "cart";
    }

    @PostMapping("cart/add")
    public String addToCart(@Valid @ModelAttribute("productCartForm") ProductCartForm productCartForm) {
        ProductDTO productDTO = this.productService.getProduct(productCartForm.getProductId());
        this.cartService.addItemToCart(productDTO, productCartForm.getQuantity());
        return "redirect:/cart";
    }

    @PostMapping("cart/update")
    public String updateCart(@Valid @ModelAttribute("productCartForm") ProductCartForm productCartForm, RedirectAttributes redirectAttributes) {
        ProductDTO productDTO = this.productService.getProduct(productCartForm.getProductId());
        this.cartService.updateItem(productDTO, productCartForm.getQuantity());
        if (productCartForm.getQuantity() == 0) {
            redirectAttributes.addFlashAttribute("cartItemRemovedMsg", String.format("%s was removed", productDTO.name()));
        } else {
            redirectAttributes.addFlashAttribute("cartItemUpdatedMsg", String.format("%s's quantity updated to %s", productDTO.name(), productCartForm.getQuantity()));
        }
        return "redirect:/cart";
    }

    @PostMapping("cart/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") UUID productId, RedirectAttributes redirectAttributes) {
        ProductDTO productDTO = this.productService.getProduct(productId);
        this.cartService.removeItem(productDTO);
        redirectAttributes.addFlashAttribute("cartItemRemovedMsg", String.format("%s was removed", productDTO.name()));
        return "redirect:/cart";
    }


    @ResponseBody
    @GetMapping("shoppingCart")
    public ShoppingCart getShoppingCart() {
        return this.cartService.getCart();
    }

    @ResponseBody
    @DeleteMapping("shoppingCart")
    public void removeItem(@ModelAttribute("productId") UUID productId) {
        ProductDTO productDTO = this.productService.getProduct(productId);
        this.cartService.removeItem(productDTO);
    }

    @ResponseBody
    @PostMapping("shoppingCart")
    public void updateItem(@ModelAttribute("productId") UUID productId, @ModelAttribute("quantity") int quantity) {
        ProductDTO productDTO = this.productService.getProduct(productId);
        this.cartService.updateItem(productDTO, quantity);
    }


}
