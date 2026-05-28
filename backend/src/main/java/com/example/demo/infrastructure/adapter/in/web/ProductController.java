package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.domain.port.in.CreateProductCommand;
import com.example.demo.domain.port.in.ProductUseCase;
import com.example.demo.domain.port.in.UpdateProductCommand;
import com.example.demo.infrastructure.adapter.in.web.dto.CreateProductRequest;
import com.example.demo.infrastructure.adapter.in.web.dto.ProductResponse;
import com.example.demo.infrastructure.adapter.in.web.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductUseCase productUseCase;

	public ProductController(ProductUseCase productUseCase) {
		this.productUseCase = productUseCase;
	}

	@PostMapping
	public Mono<ResponseEntity<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
		return productUseCase.create(toCommand(request))
				.map(ProductResponse::from)
				.map(response -> ResponseEntity
						.created(URI.create("/api/products/" + response.id()))
						.body(response));
	}

	@GetMapping("/{id}")
	public Mono<ProductResponse> findById(@PathVariable Integer id) {
		return productUseCase.findById(id)
				.map(ProductResponse::from);
	}

	@GetMapping
	public Flux<ProductResponse> findAll() {
		return productUseCase.findAll()
				.map(ProductResponse::from);
	}

	@PutMapping("/{id}")
	public Mono<ProductResponse> update(
			@PathVariable Integer id,
			@Valid @RequestBody UpdateProductRequest request
	) {
		return productUseCase.update(id, toCommand(request))
				.map(ProductResponse::from);
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable Integer id) {
		return productUseCase.delete(id)
				.thenReturn(ResponseEntity.noContent().build());
	}

	private CreateProductCommand toCommand(CreateProductRequest request) {
		return new CreateProductCommand(
				request.code(),
				request.name(),
				request.description(),
				request.price(),
				request.category(),
				request.state()
		);
	}

	private UpdateProductCommand toCommand(UpdateProductRequest request) {
		return new UpdateProductCommand(
				request.code(),
				request.name(),
				request.description(),
				request.price(),
				request.category(),
				request.state()
		);
	}

}
