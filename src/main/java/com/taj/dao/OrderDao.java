package com.taj.dao;

import java.util.List;

import com.taj.entity.Order;
import com.taj.model.ShoppingCart;

public interface OrderDao {

	public void saveOrder(ShoppingCart cart);
	
	public List<Order> showOrdersForToday();
	
	public Order getOrderDetails(int orderId);
}
