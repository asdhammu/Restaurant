package com.taj.dao;

import java.util.List;

import com.taj.entity.Category;
import com.taj.entity.Product;

public interface ProductDao {

	public Product getProductDetails(int itemId);
	
	public List<Category> getCategories();
	
}
