package my.restaurant.modal;

import lombok.Getter;
import lombok.Setter;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;

@Getter
@Setter
public class CartItem {
    private ProductDTO product;
    private PriceDTO price;
    private int quantity;
}
