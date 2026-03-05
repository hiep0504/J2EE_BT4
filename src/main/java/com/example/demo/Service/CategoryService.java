package com.example.demo.Service;

import com.example.demo.Model.CategoryModel;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CategoryService {
	private final List<CategoryModel> categories = new ArrayList<>();

	public CategoryService() {
		categories.add(new CategoryModel(1, "Điện thoại"));
		categories.add(new CategoryModel(2, "Laptop"));
	}

	public List<CategoryModel> getAllCategories() {
		return categories;
	}
}
