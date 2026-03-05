package com.example.demo.Controller;

import com.example.demo.Model.ProductModel;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String listProducts(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "product/products";
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("productModel", new ProductModel());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "product/create";
	}

	@PostMapping("/create")
	public String createProduct(@Valid @ModelAttribute("productModel") ProductModel productModel,
			BindingResult bindingResult,
			@RequestParam("imageFile") MultipartFile imageFile,
			Model model) {
		if (!imageFile.isEmpty()) {
			String fileName = imageFile.getOriginalFilename();
			if (fileName != null && fileName.length() > 200) {
				bindingResult.rejectValue("image", null, "Tên hình ảnh không quá 200 ký tự");
			}
			productModel.setImage(fileName);
		} else {
			bindingResult.rejectValue("image", null, "Tên hình ảnh không được để trống");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryService.getAllCategories());
			return "product/create";
		}

		// save image file
		try {
			Path uploadPath = Paths.get("./images/");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = imageFile.getInputStream()) {
				Path filePath = uploadPath.resolve(productModel.getImage());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		productService.addProduct(productModel);
		return "redirect:/products";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") int id, Model model) {
		ProductModel product = productService.getProductById(id);
		if (product == null)
			return "redirect:/products";
		model.addAttribute("productModel", product);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "product/edit";
	}

	@PostMapping("/edit/{id}")
	public String editProduct(@PathVariable("id") int id,
			@Valid @ModelAttribute("productModel") ProductModel productModel,
			BindingResult bindingResult,
			@RequestParam("imageFile") MultipartFile imageFile,
			Model model) {
		if (!imageFile.isEmpty()) {
			String fileName = imageFile.getOriginalFilename();
			if (fileName != null && fileName.length() > 200) {
				bindingResult.rejectValue("image", null, "Tên hình ảnh không quá 200 ký tự");
			}
			productModel.setImage(fileName);
		} else if (productModel.getImage() == null || productModel.getImage().isEmpty()) {
			bindingResult.rejectValue("image", null, "Tên hình ảnh không được để trống");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryService.getAllCategories());
			return "product/edit";
		}

		// save image file if uploaded
		if (!imageFile.isEmpty()) {
			try {
				Path uploadPath = Paths.get("./images/");
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				try (InputStream inputStream = imageFile.getInputStream()) {
					Path filePath = uploadPath.resolve(productModel.getImage());
					Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productModel.setId(id);
		productService.updateProduct(productModel);
		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") int id) {
		productService.deleteProduct(id);
		return "redirect:/products";
	}
}
