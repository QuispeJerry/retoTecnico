package com.example.demo.domain.port.out;

import com.example.demo.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {

	Mono<Product> save(Product product);

	Mono<Product> findById(Integer id);

	Flux<Product> findAll();

}
