package my.restaurant.service;

import my.restaurant.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDTO getProduct(UUID productId);
    List<ProductDTO> getFavoriteProducts();
    ProductDTO addProduct(ProductDTO productDTO);
    List<ProductDTO> searchProduct(String query);
}
