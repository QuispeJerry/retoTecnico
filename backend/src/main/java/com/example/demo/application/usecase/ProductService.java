package com.example.demo.application.usecase;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.port.in.CreateProductCommand;
import com.example.demo.domain.port.in.ProductUseCase;
import com.example.demo.domain.port.in.UpdateProductCommand;
import com.example.demo.domain.port.out.ProductRepositoryPort;
import com.example.demo.shared.exception.ProductNotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService implements ProductUseCase {

	private final ProductRepositoryPort productRepositoryPort;

	public ProductService(ProductRepositoryPort productRepositoryPort) {
		this.productRepositoryPort = productRepositoryPort;
	}

	@Override
	public Mono<Product> create(CreateProductCommand command) {
		Product product = new Product(
				null,
				command.code(),
				command.name(),
				command.description(),
				command.price(),
				command.category(),
				LocalDateTime.now(),
				null,
				command.state()
		).activateDefaults();

		return productRepositoryPort.save(product);
	}

	@Override
	public Mono<Product> findById(Integer id) {
		return findActiveById(id);
	}

	@Override
	public Flux<Product> findAll() {
		return productRepositoryPort.findAll()
				.filter(Product::state);
	}

	@Override
	public Mono<Product> update(Integer id, UpdateProductCommand command) {
		Product updates = new Product(
				id,
				command.code(),
				command.name(),
				command.description(),
				command.price(),
				command.category(),
				null,
				null,
				command.state()
		);

		return findActiveById(id)
				.map(current -> current.updateWith(updates))
				.flatMap(productRepositoryPort::save);
	}

	@Override
	public Mono<Void> delete(Integer id) {
		return findActiveById(id)
				.map(Product::deactivate)
				.flatMap(productRepositoryPort::save)
				.then();
	}

	private Mono<Product> findActiveById(Integer id) {
		return productRepositoryPort.findById(id)
				.filter(Product::state)
				.switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
	}

}
