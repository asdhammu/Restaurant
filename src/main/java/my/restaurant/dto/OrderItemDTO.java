package my.restaurant.dto;

public record OrderItemDTO(long orderItemId, ProductDTO product, PriceDTO price, int quantity, PriceDTO subtotal) {
}
