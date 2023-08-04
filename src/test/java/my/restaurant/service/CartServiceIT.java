package my.restaurant.service;

import my.restaurant.dto.CategoryDTO;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;
import my.restaurant.entity.Cart;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.security.WithMockCustomUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockCustomUser
public class CartServiceIT {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private Cart cart;


    @Before
    public void setUp(){
        cartService.initCart("username");
    }

    @Test
    public void emptyShoppingCart(){
        CategoryDTO categoryDTO = this.categoryService.addCategory(new CategoryDTO(0, "category"));
        ProductDTO productDTO = this.productService.addProduct(new ProductDTO(null, "p-name", "test", "description", new PriceDTO(20.0f), categoryDTO));
        this.cartService.addItemToCart(productDTO, 1);

        this.cartService.clearShoppingCart();

        ShoppingCart shoppingCart = this.cartService.getCart();
        assertTrue(shoppingCart.getCartItems().isEmpty());

    }

}
