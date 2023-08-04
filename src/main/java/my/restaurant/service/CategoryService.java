package my.restaurant.service;

import my.restaurant.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getFullMenu();
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO getCategory(long categoryId);
}
