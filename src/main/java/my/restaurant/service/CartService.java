package my.restaurant.service;

import my.restaurant.dto.ProductDTO;
import my.restaurant.modal.ShoppingCart;

public interface CartService {
    ShoppingCart getCart();

    void addItemToCart(ProductDTO productDTO, int quantity);

    void removeItem(ProductDTO productDTO);

    void updateItem(ProductDTO productDTO, int quantity);

    void clearShoppingCart();

    /**
     * This method will merge the current cart is session with
     * the existing shopping cart of the user saved in db
     */
    void mergeShoppingCart(ShoppingCart shoppingCart, String username);
    void initCart(String  username);
}
