package com.taj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taj.dao.ProductDao;
import com.taj.entity.Category;

@Controller
class HomepageController {

	@Autowired
	private ProductDao productDao;

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}


	@RequestMapping(value = { "/", "/index", "/welcome" }, method = RequestMethod.GET)
	public String homePage(ModelMap model) {

		model.addAttribute("message", "Taj Restaurant");

		List<Category> categories = this.productDao.getCategories();

		model.addAttribute("leftCategories", categories.subList(0, categories.size() / 2));

		model.addAttribute("rightCategories", categories.subList(categories.size() / 2 + 1, categories.size()));

		return "index";
	}

	

	
	
}
