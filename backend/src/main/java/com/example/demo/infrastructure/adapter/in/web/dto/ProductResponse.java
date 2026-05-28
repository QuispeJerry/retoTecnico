package com.example.demo.infrastructure.adapter.in.web.dto;

import com.example.demo.domain.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
		Integer id,
		String code,
		String name,
		String description,
		BigDecimal price,
		String category,
		LocalDateTime regDate,
		LocalDateTime modDate,
		Boolean state
) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.id(),
				product.code(),
				product.name(),
				product.description(),
				product.price(),
				product.category(),
				product.regDate(),
				product.modDate(),
				product.state()
		);
	}

}
