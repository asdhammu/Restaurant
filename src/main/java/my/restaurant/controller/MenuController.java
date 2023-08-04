package my.restaurant.controller;

import my.restaurant.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    private final CategoryService categoryService;

    public MenuController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("menu")
    public String getMenu(ModelMap modelMap) {
        modelMap.addAttribute("menu", this.categoryService.getFullMenu());
        return "menu";
    }
}
