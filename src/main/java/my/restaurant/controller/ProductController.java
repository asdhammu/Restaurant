package my.restaurant.controller;

import my.restaurant.dto.ProductDTO;
import my.restaurant.modal.forms.ProductCartForm;
import my.restaurant.service.ProductService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("product/{id}")
    public String product(@PathVariable UUID id, ModelMap modelMap) {
        modelMap.put("product", this.productService.getProduct(id));
        ProductCartForm productCartForm = new ProductCartForm();
        productCartForm.setProductId(id);
        modelMap.put("productCartForm", productCartForm);
        return "product/product";
    }

    @GetMapping("/product/search")
    @ResponseBody
    public List<ProductDTO> searchProduct(@Param("query") String query) {
        return this.productService.searchProduct(query);
    }

}
