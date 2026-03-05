package com.example.demo.Model;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
	private int id;

	@NotBlank(message = "Tên sản phẩm không được để trống")
	private String name;

	@NotNull(message = "Giá sản phẩm không được để trống")
	@Min(value = 1, message = "Giá sản phẩm không được nhỏ hơn 1")
	@Max(value = 9999999, message = "Giá sản phẩm tối đa là 9,999,999")
	private Integer price;

	private String image;

	private Integer categoryId;
}
