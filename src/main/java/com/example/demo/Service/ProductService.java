package com.example.demo.Service;

import com.example.demo.Model.ProductModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProductService {
	private final List<ProductModel> products = new ArrayList<>();
	private int nextId = 3;

	public ProductService() {
		products.add(new ProductModel(1, "laptop 1", 30000, "laptop.jpg", 2));
		products.add(new ProductModel(2, "dien thoai 1", 20000, "phone.jpg", 1));
	}

	public List<ProductModel> getAllProducts() {
		return products;
	}

	public void addProduct(@Valid ProductModel product) {
		product.setId(nextId++);
		products.add(product);
	}

	public ProductModel getProductById(int id) {
		return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
	}

	public void updateProduct(@Valid ProductModel updatedProduct) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getId() == updatedProduct.getId()) {
				products.set(i, updatedProduct);
				return;
			}
		}
	}

	public void deleteProduct(int id) {
		products.removeIf(product -> product.getId() == id);
	}
}
