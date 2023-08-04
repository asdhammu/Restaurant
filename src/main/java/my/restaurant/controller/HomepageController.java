package my.restaurant.controller;

import my.restaurant.service.CarouselService;
import my.restaurant.service.ProductService;
import my.restaurant.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class HomepageController {

    private final ProductService productService;
    private final CarouselService carouselService;

    HomepageController(ProductService productService, CarouselService carouselService) {
        this.productService = productService;
        this.carouselService = carouselService;
    }

    @GetMapping(value = {"/", "/home", "index"})
    public String homepage(ModelMap map) {
        map.addAttribute(Constants.PageTitle, "My Restaurant");
        map.addAttribute("carousel", this.carouselService.getCarousel());
        map.addAttribute("favorites", this.productService.getFavoriteProducts());
        return "home";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }
}
