package com.example.demo.domain.port.in;

import java.math.BigDecimal;

public record CreateProductCommand(
		String code,
		String name,
		String description,
		BigDecimal price,
		String category,
		Boolean state
) {
}
