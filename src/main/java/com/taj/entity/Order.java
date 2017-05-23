package com.taj.entity;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "userorder")
public class Order {

	@Id
	@Column(name = "order_id")
	private int orderId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@OneToMany(mappedBy = "orderId")
	private List<OrderItems> items;

	@Column(name = "price")
	private double price;

	@Column(name="order_date")
	private Timestamp date;
	
	
	public Order() {
		// TODO Auto-generated constructor stub
	}
	
	public Order(User user){
		this.userId = user;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public List<OrderItems> getItems() {
		return items;
	}

	public void setItems(List<OrderItems> items) {
		this.items = items;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	

	
}
