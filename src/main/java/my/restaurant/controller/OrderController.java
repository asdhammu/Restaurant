package my.restaurant.controller;

import jakarta.validation.Valid;
import my.restaurant.modal.forms.OrderLookupForm;
import my.restaurant.service.OrderService;
import my.restaurant.utils.Constants;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("user/orders")
    @PreAuthorize("hasAuthority('USER')")
    public String orders(@Param("page") Optional<Integer> page, @Param("size") Optional<Integer> size, Model model) {
        model.addAttribute(Constants.PageTitle, "List of Orders");
        model.addAttribute("orders", this.orderService.getOrders(page.orElse(0), size.orElse(50)));
        return "order/orderList";
    }

    @GetMapping("user/orders/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String ordersById(@PathVariable UUID id, Model model) {
        model.addAttribute(Constants.PageTitle, String.format("Order details for %s", id));
        model.addAttribute("order", this.orderService.getOrder(id));
        return "order/orderDetails";
    }

    @GetMapping("order/confirmation")
    public String orderPlaced(@RequestParam("id") UUID uuid, @RequestParam("emailId") String emailId, Model model) {
        model.addAttribute(Constants.PageTitle, String.format("Order placed %s", uuid));
        model.addAttribute("order", this.orderService.getOrder(uuid, emailId));
        model.addAttribute("message", "Congratulations!, Your order has been placed.");
        return "order/orderConfirmation";
    }

    @GetMapping("order/lookup")
    public String orderLookup(Model model) {
        model.addAttribute(Constants.PageTitle, "Order Lookup");
        model.addAttribute("orderLookupForm", new OrderLookupForm());
        return "order/orderLookup";
    }


    @PostMapping("order/lookup")
    public String orderLookup(@Valid @ModelAttribute("orderLookupForm") OrderLookupForm orderLookupForm, BindingResult bindingResult, Model model) {
        model.addAttribute(Constants.PageTitle, "Order Lookup");
        if (bindingResult.hasErrors()) {
            return "order/orderLookup";
        }
        model.addAttribute("order", this.orderService.getOrder(orderLookupForm.getOrderNumber(), orderLookupForm.getEmailId()));
        return "order/orderLookup";
    }

}
