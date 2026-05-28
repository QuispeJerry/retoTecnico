package com.example.demo.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.port.in.ProductUseCase;
import com.example.demo.shared.exception.ProductNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private ProductUseCase productUseCase;

	@Test
	void createShouldReturnCreatedProduct() {
		when(productUseCase.create(any())).thenReturn(Mono.just(product(1, "FND001")));

		webTestClient.post()
				.uri("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(validRequest("FND001"))
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().location("/api/products/1")
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.code").isEqualTo("FND001");
	}

	@Test
	void findByIdShouldReturnProduct() {
		when(productUseCase.findById(1)).thenReturn(Mono.just(product(1, "FND001")));

		webTestClient.get()
				.uri("/api/products/1")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.code").isEqualTo("FND001");
	}

	@Test
	void findAllShouldReturnProducts() {
		when(productUseCase.findAll()).thenReturn(Flux.just(product(1, "FND001"), product(2, "FND002")));

		webTestClient.get()
				.uri("/api/products")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].code").isEqualTo("FND001")
				.jsonPath("$[1].code").isEqualTo("FND002");
	}

	@Test
	void updateShouldReturnUpdatedProduct() {
		when(productUseCase.update(any(), any())).thenReturn(Mono.just(product(1, "FND009")));

		webTestClient.put()
				.uri("/api/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(validRequest("FND009"))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1)
				.jsonPath("$.code").isEqualTo("FND009");
	}

	@Test
	void deleteShouldReturnNoContent() {
		when(productUseCase.delete(1)).thenReturn(Mono.empty());

		webTestClient.delete()
				.uri("/api/products/1")
				.exchange()
				.expectStatus().isNoContent()
				.expectBody().isEmpty();
	}

	@Test
	void invalidRequestShouldReturnBadRequest() {
		webTestClient.post()
				.uri("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(Map.of(
						"code", "",
						"name", "",
						"price", "-1",
						"category", ""
				))
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.message").isEqualTo("Invalid request body");
	}

	@Test
	void productNotFoundShouldReturnNotFound() {
		when(productUseCase.findById(99)).thenReturn(Mono.error(new ProductNotFoundException(99)));

		webTestClient.get()
				.uri("/api/products/99")
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.status").isEqualTo(404);
	}

	@Test
	void duplicatedCodeShouldReturnConflict() {
		when(productUseCase.create(any())).thenReturn(Mono.error(new DuplicateKeyException("duplicated code")));

		webTestClient.post()
				.uri("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(validRequest("FND001"))
				.exchange()
				.expectStatus().isEqualTo(409)
				.expectBody()
				.jsonPath("$.status").isEqualTo(409);
	}

	private Map<String, Object> validRequest(String code) {
		return Map.of(
				"code", code,
				"name", "Fondo Premium",
				"description", "Producto de prueba",
				"price", "100.50",
				"category", "Fondos",
				"state", true
		);
	}

	private Product product(Integer id, String code) {
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
