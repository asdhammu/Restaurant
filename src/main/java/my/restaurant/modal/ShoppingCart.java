package my.restaurant.modal;

import lombok.Getter;
import lombok.Setter;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;

import java.io.DataInput;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ShoppingCart {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private List<CartItem> cartItems = new ArrayList<>();
    private float price = 0;
    private float tax = 0;
    private float shipping = 0;
    private String currency = "$";

    public void addItem(ProductDTO productDTO, int quantity) {
        Optional<CartItem> cartItemOptional = this.cartItems.stream().filter(x -> x.getProduct().productId().compareTo(productDTO.productId()) == 0).findAny();
        if (cartItemOptional.isPresent()) {
            CartItem item = cartItemOptional.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setQuantity(quantity);
            item.setProduct(productDTO);
            item.setPrice(productDTO.price());
            cartItems.add(item);
        }
        this.reprice();
    }

    /**
     * Price can vary with productDTO price coz productDTO will show the current price of product
     * this will help to warn the user on cart that the price has changed
     * This method is used when the user is logged in and added product to cart at certain price
     */
    public void addItem(ProductDTO productDTO, int quantity, PriceDTO price) {
        CartItem item = new CartItem();
        item.setQuantity(quantity);
        item.setProduct(productDTO);
        item.setPrice(price);
        cartItems.add(item);
        this.reprice();
    }

    public void updateQuantity(ProductDTO productDTO, int quantity) {
        if (quantity < 1) {
            this.removeItem(productDTO);
            return;
        }
        Optional<CartItem> cartItemOptional = this.cartItems.stream().filter(x -> x.getProduct().productId().compareTo(productDTO.productId()) == 0).findAny();
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
        }
        this.reprice();
    }

    public void removeItem(ProductDTO productDTO) {
        Optional<CartItem> cartItemOptional = this.cartItems.stream().filter(x -> x.getProduct().productId().compareTo(productDTO.productId()) == 0).findAny();
        cartItemOptional.ifPresent(cartItem -> this.cartItems.remove(cartItem));
        this.reprice();
    }

    private void reprice() {
        float reprice = 0;
        for (CartItem cartItem : cartItems) {
            reprice += cartItem.getQuantity() * cartItem.getPrice().price();
        }
        this.price = Float.parseFloat(decimalFormat.format(reprice));
    }

    public void clear() {
        this.cartItems.clear();
        this.price = 0;
    }

    public boolean isEmpty() {
        return this.getCartItems().isEmpty();
    }

    public int getCount() {
        int sum = 0;
        for (CartItem cartItem : cartItems) {
            sum += cartItem.getQuantity();
        }
        return sum;
    }
}
