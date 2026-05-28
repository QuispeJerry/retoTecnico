package com.example.demo.domain.port.in;

import com.example.demo.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductUseCase {

	Mono<Product> create(CreateProductCommand command);

	Mono<Product> findById(Integer id);

	Flux<Product> findAll();

	Mono<Product> update(Integer id, UpdateProductCommand command);

	Mono<Void> delete(Integer id);

}
