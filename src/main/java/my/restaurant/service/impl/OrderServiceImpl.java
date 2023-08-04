package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.*;
import my.restaurant.entity.*;
import my.restaurant.modal.OrderStatus;
import my.restaurant.modal.ShoppingCart;
import my.restaurant.repository.OrderRepository;
import my.restaurant.repository.UserRepository;
import my.restaurant.service.AuthenticationFacade;
import my.restaurant.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, AuthenticationFacade authenticationFacade, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderDTO> getOrders(int page, int size) {
        Page<Order> orders = this.orderRepository.getOrderByUserUsername(this.authenticationFacade.getUsername(),
                PageRequest.of(page, size, Sort.by("updated").descending()));
        return orders.stream().map(order -> new OrderDTO(order.getOrderId(), null, new PriceDTO(order.getTotalPrice()), new PriceDTO(0),
                new PriceDTO(order.getTax()), order.getCreated(), order.getOrderStatus(), null, null)).toList();
    }

    @Override
    public OrderDTO getOrder(UUID orderId) {
        Order order = this.orderRepository.getOrderByOrderIdAndUserUsername(orderId, this.authenticationFacade.getUsername());
        List<OrderItemDTO> items = new ArrayList<>();

        float subtotal = 0;
        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            ProductDTO productDTO = new ProductDTO(product.getProductId(), product.getImgUrl(), product.getProductName(), product.getDescription());
            float orderItemSubtotal = orderItem.getPrice() * orderItem.getQuantity();
            OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem.getOrderItemId(), productDTO, new PriceDTO(orderItem.getPrice()), orderItem.getQuantity(),
                    new PriceDTO(orderItemSubtotal));
            items.add(orderItemDTO);
            subtotal += orderItemSubtotal;
        }

        Address address = order.getBillingAddress();
        AddressDTO addressDTO = new AddressDTO(address.firstName, address.lastName, address.emailId, address.addressId, address.streetAddress1,
                address.streetAddress2, address.country.getName(), address.state.getName(), address.city, address.postalCode, false, null, address.phoneNumber);

        PaymentMethod paymentMethod = order.getPaymentMethod();
        PaymentDTO paymentDTO = new PaymentDTO(paymentMethod.getPaymentMethodId(), paymentMethod.getCreditCardNumber(), paymentMethod.getCvv(),
                paymentMethod.getFullName(), paymentMethod.getExpiryMonth(), paymentMethod.getExpiryMonth(), false);
        return new OrderDTO(order.getOrderId(), items, new PriceDTO(order.getTotalPrice()),
                new PriceDTO(subtotal), new PriceDTO(order.getTax()), order.getCreated(), order.getOrderStatus(), addressDTO,
                paymentDTO);
    }

    @Override
    public OrderDTO getOrder(UUID orderId, String emailId) {
        Optional<Order> orderOptional = this.orderRepository.getOrderByOrderIdAndBillingAddressEmailId(orderId, emailId);
        if (orderOptional.isEmpty()) {
            LOGGER.info(String.format("No order for orderId %s and email %s", orderId, emailId));
            return null;
        }
        Order order = orderOptional.get();
        return new OrderDTO(order.getOrderId(), null, new PriceDTO(order.getTotalPrice()),
                new PriceDTO(0), new PriceDTO(order.getTax()), order.getCreated(), order.getOrderStatus(), null, null);
    }

    @Override
    public OrderDTO addOrder(ShoppingCart shoppingCart, AddressDTO addressDTO, PaymentDTO paymentDTO) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setOrderStatus(OrderStatus.RECEIVED);
        if (authenticationFacade.isLoggedIn()) {
            Optional<User> userOptional = this.userRepository.findByUsername(this.authenticationFacade.getUsername());
            userOptional.ifPresent(order::setUser);
        }

        order.setTax(shoppingCart.getTax());
        order.setTotalPrice(shoppingCart.getPrice());
        shoppingCart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice().price());
            Product product = new Product();
            product.setProductId(cartItem.getProduct().productId());
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        });
        order.setBillingAddress(new Address(addressDTO.id()));
        order.setPaymentMethod(new PaymentMethod(paymentDTO.id()));
        Order persistedOrder = this.orderRepository.save(order);
        return new OrderDTO(persistedOrder.getOrderId());
    }
}
