package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.port.out.ProductRepositoryPort;
import com.example.demo.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.example.demo.infrastructure.adapter.out.persistence.repository.ProductReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

	private final ProductReactiveRepository productReactiveRepository;

	public ProductPersistenceAdapter(ProductReactiveRepository productReactiveRepository) {
		this.productReactiveRepository = productReactiveRepository;
	}

	@Override
	public Mono<Product> save(Product product) {
		return productReactiveRepository.save(toEntity(product))
				.map(this::toDomain);
	}

	@Override
	public Mono<Product> findById(Integer id) {
		return productReactiveRepository.findById(id)
				.map(this::toDomain);
	}

	@Override
	public Flux<Product> findAll() {
		return productReactiveRepository.findAll()
				.map(this::toDomain);
	}

	private ProductEntity toEntity(Product product) {
		return new ProductEntity(
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

	private Product toDomain(ProductEntity entity) {
		return new Product(
				entity.getId(),
				entity.getCode(),
				entity.getName(),
				entity.getDescription(),
				entity.getPrice(),
				entity.getCategory(),
				entity.getRegDate(),
				entity.getModDate(),
				entity.getState()
		);
	}

}
