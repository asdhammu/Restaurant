package com.taj.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.taj.dao.OrderDao;
import com.taj.entity.Order;
import com.taj.entity.OrderItems;
import com.taj.entity.User;
import com.taj.model.ItemInfo;
import com.taj.model.ShoppingCart;

public class OrderDaoImpl implements OrderDao {

	private SessionFactory sessionFactory;

	public void saveOrder(ShoppingCart cart) {	

		Session session = this.sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		User user = null;

		if (this.getUser(cart.getCustomerInfo().getEmail()) == null) {
			user = new User();
			user.setfName(cart.getCustomerInfo().getfName());
			user.setlName(cart.getCustomerInfo().getlName());
			user.setEmailId(cart.getCustomerInfo().getEmail());
			user.setPhone(cart.getCustomerInfo().getPhone());
			user.setAddress(cart.getCustomerInfo().getAddress());
			session.persist(user);			
		}else{
			user = this.getUser(cart.getCustomerInfo().getEmail());
		}
		
				
				
		Order order = new Order(user);
		order.setOrderId(this.getMaxOrderNum()+1);
		order.setPrice(cart.getTotalPrice());
		order.setDate(new Timestamp(System.currentTimeMillis()));
		
		session.persist(order);
		
		for (ItemInfo info : cart.getCartItem()) {
			OrderItems items = new OrderItems(order);
			items.setQuantity(info.getQuantity());
			items.setName(info.getProductInfo().getProductName());
			items.setHotnessLevel(info.getHotnessLevel());
			session.persist(items);
		}
		
		
		transaction.commit();
		session.close();
		
		cart.setOrderNumber(order.getOrderId());
		
	}

	private User getUser(String emailId) {
		Session session = this.sessionFactory.openSession();
		String sql = "from User where emailId=:emailId";
		Query query = session.createQuery(sql);
		query.setParameter("emailId", emailId);
		Object val = (Object) query.uniqueResult();
		
		session.close();

		return (User) val;

	}

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderId) from " + Order.class.getName() + " o ";
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(sql);
		Integer value = (Integer) query.uniqueResult();
		if (value == null) {
			return 0;
		}
		session.close();
		return value;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Order> showOrdersForToday() {
		
		String sql = "from Order where date>=:startDate and date<=:endDate";
		Date date = new Date(new Date().getTime()-24*3600*1000);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(sql);
		query.setTimestamp("startDate", new Timestamp(date.getTime()));
		query.setTimestamp("endDate", new Timestamp(System.currentTimeMillis()));
		List<Order> orders = query.list();
		session.close();
		return orders;
		
	}

	public Order getOrderDetails(int orderId){
		String sql = "from Order where orderId=:orderId";
		Session session = this.sessionFactory.openSession();
		Query query = session.createQuery(sql);
		query.setParameter("orderId", orderId);
		
		Order order = (Order) query.uniqueResult();
		session.close();
		return order;
		
	}
}
