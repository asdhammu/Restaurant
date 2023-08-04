package my.restaurant.service;

import my.restaurant.dto.AddressDTO;
import my.restaurant.dto.OrderDTO;
import my.restaurant.dto.PaymentDTO;
import my.restaurant.modal.ShoppingCart;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDTO> getOrders(int page, int size);
    OrderDTO getOrder(UUID orderId);
    OrderDTO getOrder(UUID orderId, String emailId);
    OrderDTO addOrder(ShoppingCart shoppingCart, AddressDTO addressDTO, PaymentDTO paymentDTO);
}
