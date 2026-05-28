package com.example.demo.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
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

	public Product activateDefaults() {
		return new Product(id, code, name, description, price, category, regDate, modDate, state == null || state);
	}

	public Product updateWith(Product product) {
		return new Product(
				id,
				product.code(),
				product.name(),
				product.description(),
				product.price(),
				product.category(),
				regDate,
				LocalDateTime.now(),
				product.state() == null ? state : product.state()
		);
	}

	public Product deactivate() {
		return new Product(id, code, name, description, price, category, regDate, LocalDateTime.now(), false);
	}

}
