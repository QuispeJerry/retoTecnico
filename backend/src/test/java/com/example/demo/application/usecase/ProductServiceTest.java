package com.example.demo.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.port.in.CreateProductCommand;
import com.example.demo.domain.port.in.UpdateProductCommand;
import com.example.demo.domain.port.out.ProductRepositoryPort;
import com.example.demo.shared.exception.ProductNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepositoryPort productRepositoryPort;

	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepositoryPort);
	}

	@Test
	void createShouldPersistProductWithDefaults() {
		CreateProductCommand command = new CreateProductCommand(
				"FND001",
				"Fondo Premium",
				"Producto de prueba",
				new BigDecimal("100.50"),
				"Fondos",
				null
		);

		when(productRepositoryPort.save(any(Product.class)))
				.thenAnswer(invocation -> {
					Product product = invocation.getArgument(0);
					return Mono.just(new Product(
							1,
							product.code(),
							product.name(),
							product.description(),
							product.price(),
							product.category(),
							product.regDate(),
							product.modDate(),
							product.state()
					));
				});

		StepVerifier.create(productService.create(command))
				.assertNext(product -> {
					assertThat(product.id()).isEqualTo(1);
					assertThat(product.code()).isEqualTo("FND001");
					assertThat(product.state()).isTrue();
					assertThat(product.regDate()).isNotNull();
					assertThat(product.modDate()).isNull();
				})
				.verifyComplete();
	}

	@Test
	void findByIdShouldReturnProductWhenExists() {
		Product product = activeProduct(1, "FND001");
		when(productRepositoryPort.findById(1)).thenReturn(Mono.just(product));

		StepVerifier.create(productService.findById(1))
				.expectNext(product)
				.verifyComplete();
	}

	@Test
	void findByIdShouldFailWhenProductDoesNotExist() {
		when(productRepositoryPort.findById(99)).thenReturn(Mono.empty());

		StepVerifier.create(productService.findById(99))
				.expectError(ProductNotFoundException.class)
				.verify();
	}

	@Test
	void findAllShouldReturnOnlyActiveProducts() {
		Product active = activeProduct(1, "FND001");
		Product inactive = new Product(
				2,
				"FND002",
				"Fondo Inactivo",
				"Producto inactivo",
				new BigDecimal("50.00"),
				"Fondos",
				LocalDateTime.now(),
				null,
				false
		);

		when(productRepositoryPort.findAll()).thenReturn(Flux.just(active, inactive));

		StepVerifier.create(productService.findAll())
				.expectNext(active)
				.verifyComplete();
	}

	@Test
	void updateShouldPersistUpdatedProduct() {
		Product current = activeProduct(1, "FND001");
		UpdateProductCommand command = new UpdateProductCommand(
				"FND009",
				"Fondo Actualizado",
				"Descripcion actualizada",
				new BigDecimal("250.75"),
				"Fondos Mutuos",
				true
		);

		when(productRepositoryPort.findById(1)).thenReturn(Mono.just(current));
		when(productRepositoryPort.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

		StepVerifier.create(productService.update(1, command))
				.assertNext(product -> {
					assertThat(product.id()).isEqualTo(1);
					assertThat(product.code()).isEqualTo("FND009");
					assertThat(product.name()).isEqualTo("Fondo Actualizado");
					assertThat(product.regDate()).isEqualTo(current.regDate());
					assertThat(product.modDate()).isNotNull();
					assertThat(product.state()).isTrue();
				})
				.verifyComplete();
	}

	@Test
	void deleteShouldDeactivateProduct() {
		Product current = activeProduct(1, "FND001");
		when(productRepositoryPort.findById(1)).thenReturn(Mono.just(current));
		when(productRepositoryPort.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

		StepVerifier.create(productService.delete(1))
				.verifyComplete();

		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(productRepositoryPort).save(productCaptor.capture());
		Product deletedProduct = productCaptor.getValue();
		assertThat(deletedProduct.state()).isFalse();
		assertThat(deletedProduct.modDate()).isNotNull();
	}

	private Product activeProduct(Integer id, String code) {
		return new Product(
				id,
				code,
				"Fondo Premium",
				"Producto de prueba",
				new BigDecimal("100.50"),
				"Fondos",
				LocalDateTime.now(),
				null,
				true
		);
	}

}
