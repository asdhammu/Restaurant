package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.CategoryDTO;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;
import my.restaurant.entity.Category;
import my.restaurant.entity.Product;
import my.restaurant.exceptions.CategoryNotFoundException;
import my.restaurant.exceptions.ProductNotFountException;
import my.restaurant.repository.CategoryRepository;
import my.restaurant.repository.ProductRepository;
import my.restaurant.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO getProduct(UUID productId) {
        Optional<Product> productOptional = this.productRepository.getProductByProductId(productId);
        if (productOptional.isEmpty()) {
            throw new ProductNotFountException("No product found for id " + productId);
        }
        Product product = productOptional.get();
        Category category = product.getCategory();
        CategoryDTO categoryDTO = null;
        if (category != null) {
            categoryDTO = new CategoryDTO(category.getCategoryId(), category.getCategoryName(), null);
        }
        return new ProductDTO(product.getProductId(), product.getProductName(), "/img/product/product1.jpg", product.getDescription(), new PriceDTO(product.getPrice()), categoryDTO);
    }

    @Override
    public List<ProductDTO> getFavoriteProducts() {
        List<Product> products = this.productRepository.getProductsByFavoriteTrue();
        return products.stream().map(product -> {
            Category category = product.getCategory();
            CategoryDTO categoryDTO = null;
            if (category != null) {
                categoryDTO = new CategoryDTO(category.getCategoryId(), category.getCategoryName(), null);
            }
            return new ProductDTO(product.getProductId(), product.getProductName(), "/img/product/product1.jpg", product.getDescription(), new PriceDTO(product.getPrice()), categoryDTO);
        }).toList();

    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Optional<Category> category = this.categoryRepository.findById(productDTO.category().id());
        if (category.isEmpty())
            throw new CategoryNotFoundException(String.format("No category for %s", productDTO.category().id()));
        Product product = new Product();
        product.setProductName(productDTO.name());
        product.setPrice(productDTO.price().price());
        product.setCategory(new Category(productDTO.category().id()));
        product.setImgUrl(productDTO.imgUrl());
        product.setFavorite(false);
        product.setDescription(productDTO.description());
        Product persistedProduct = this.productRepository.save(product);
        return new ProductDTO(persistedProduct.getProductId(), new PriceDTO(persistedProduct.getPrice()));
    }

    @Override
    public List<ProductDTO> searchProduct(String query) {
        if (query != null) {
            query += ":*";
        }
        List<Product> products = this.productRepository.searchProduct(query);
        return products.stream().map(product -> new ProductDTO(product.getProductId(), product.getProductName(), "", product.getDescription(),
                null, null)).collect(Collectors.toList());
    }
}
