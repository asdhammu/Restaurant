package com.taj.model;

import com.taj.entity.Product;

public class ProductInfo {

	private String productCode;
	private String productName;
	private double price;
	private String description;
	private int categoryId;

	public ProductInfo(Product product) {

		this.productCode = String.valueOf(product.getProductId());
		this.productName = product.getProductName();
		this.price = product.getPrice();
		this.description = product.getDescription();
		this.categoryId = product.getCategoryId().getCategoryId();
	}
	
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	
	
}
