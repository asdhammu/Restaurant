package my.restaurant.controller;

import my.restaurant.dto.CategoryDTO;
import my.restaurant.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("category/{id}")
    public String category(@PathVariable long id, ModelMap model) {
        CategoryDTO categoryDTO = this.categoryService.getCategory(id);
        model.addAttribute("category", categoryDTO);
        return "category/category";
    }
}
