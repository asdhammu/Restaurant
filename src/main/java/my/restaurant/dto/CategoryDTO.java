package my.restaurant.dto;

import java.util.List;

public record CategoryDTO(long id, String name, List<ProductDTO> products) {
    public CategoryDTO(long id, String name){
        this(id, name, null);
    }
}
