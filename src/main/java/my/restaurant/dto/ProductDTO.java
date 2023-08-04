package my.restaurant.dto;


import java.util.UUID;

public record ProductDTO(UUID productId, String name, String imgUrl, String description, PriceDTO price,
                         CategoryDTO category) {
    public ProductDTO(UUID productId, PriceDTO price) {
        this(productId, null, "/img/product/product1.jpg", null, price, null);
    }

    public ProductDTO(UUID productId, String imgUrl, String name, String description) {
        this(productId, name, "/img/product/product1.jpg", description, null, null);
    }
}
