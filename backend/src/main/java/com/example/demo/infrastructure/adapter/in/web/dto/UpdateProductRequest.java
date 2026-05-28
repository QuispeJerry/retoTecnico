package com.example.demo.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateProductRequest(
		@NotBlank
		@Size(max = 10)
		String code,

		@NotBlank
		@Size(max = 100)
		String name,

		@Size(max = 200)
		String description,

		@NotNull
		@DecimalMin(value = "0.00", inclusive = false)
		@Digits(integer = 8, fraction = 2)
		BigDecimal price,

		@NotBlank
		@Size(max = 100)
		String category,

		Boolean state
) {
}
