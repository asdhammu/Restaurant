package com.taj.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taj.dao.OrderDao;
import com.taj.entity.Order;
import com.taj.model.CustomerInfo;
import com.taj.model.ShoppingCart;
import com.taj.utils.TajUtils;
import com.taj.validator.CustomerInfoValidator;

@Controller
public class OrderController {
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private CustomerInfoValidator customerInfoValidator;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public CustomerInfoValidator getCustomerInfoValidator() {
		return customerInfoValidator;
	}

	public void setCustomerInfoValidator(CustomerInfoValidator customerInfoValidator) {
		this.customerInfoValidator = customerInfoValidator;
	}


	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String checkout(HttpServletRequest httpServletRequest, Model map) {

		ShoppingCart cart = TajUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:shoppingCart";
		}

		CustomerInfo customerInfo = new CustomerInfo();

		map.addAttribute("customerForm", customerInfo);

		return "checkout";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkout(HttpServletRequest httpServletRequest, Model map,
			@ModelAttribute("customerForm") CustomerInfo customerForm, BindingResult bindingResult) {

		customerInfoValidator.validate(customerForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "checkout";
		}

		ShoppingCart cart = TajUtils.getOrderFromSession(httpServletRequest);

		cart.setCustomerInfo(customerForm);

		return "redirect:/orderConfirmation";

	}

	@RequestMapping(value = "/orderConfirmation", method = RequestMethod.GET)
	public String orderConfirmation(HttpServletRequest httpServletRequest, ModelMap map) {

		ShoppingCart cart = TajUtils.getOrderFromSession(httpServletRequest);
		if (cart.isEmpty()) {
			return "redirect:/shoppingCart";
		}

		map.addAttribute("cart", cart);

		return "orderConfirmation";

	}

	@RequestMapping(value = "placeOrder", method = RequestMethod.GET)
	public String placeOrder(HttpServletRequest httpServletRequest, Model model) {

		ShoppingCart cart = TajUtils.getOrderFromSession(httpServletRequest);

		if (cart.isEmpty()) {
			return "redirect:/shoppingCart";
		}

		this.orderDao.saveOrder(cart);

		TajUtils.removeOrderFromSession(httpServletRequest);

		model.addAttribute("orderId", cart.getOrderNumber());

		return "orderPlaced";
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String findOrder(@RequestParam(value = "v", required = true) String v, Model map) {

		if (!v.equalsIgnoreCase("today")) {
			map.addAttribute("error", "Bad Input");
		} else {

			List<Order> list = this.orderDao.showOrdersForToday();
			map.addAttribute("orders", list);

		}
		return "orderList";
	}

	@RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
	public String getOrderDetails(@RequestParam(value = "o", required = true) String id, Model map) {

		
		
		map.addAttribute("order", this.orderDao.getOrderDetails(Integer.parseInt(id)));
		return "orderDetails";

	}

	
}
