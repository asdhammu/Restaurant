package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.CategoryDTO;
import my.restaurant.dto.PriceDTO;
import my.restaurant.dto.ProductDTO;
import my.restaurant.entity.Category;
import my.restaurant.entity.Product;
import my.restaurant.exceptions.CategoryExistsException;
import my.restaurant.exceptions.CategoryNotFoundException;
import my.restaurant.repository.CategoryRepository;
import my.restaurant.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getFullMenu() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName(),
                category.getProducts().stream().map(product -> new ProductDTO(product.getProductId(), product.getProductName(), "", product.getDescription(),
                        new PriceDTO(product.getPrice()), null)).toList())).toList();
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = this.categoryRepository.findCategoryByCategoryName(categoryDTO.name());

        if (categoryOptional.isPresent())
            throw new CategoryExistsException(String.format("Category exists for %s", categoryDTO.name()));

        Category category = new Category();
        category.setCategoryName(categoryDTO.name());
        category = this.categoryRepository.save(category);
        return new CategoryDTO(category.getCategoryId(), category.getCategoryName());
    }

    @Override
    public CategoryDTO getCategory(long categoryId) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty())
            throw new CategoryNotFoundException(String.format("No category for %s", categoryId));

        Category category = categoryOptional.get();

        List<ProductDTO> products = new ArrayList<>();
        for (Product product : category.getProducts()) {
            ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getProductName(), "/img/product/product1.jpg", product.getDescription(),
                    new PriceDTO(product.getPrice()), null);
            products.add(productDTO);
        }
        return new CategoryDTO(category.getCategoryId(), category.getCategoryName(), products);
    }

}
