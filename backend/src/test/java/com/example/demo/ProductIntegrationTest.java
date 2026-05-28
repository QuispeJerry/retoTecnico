package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.infrastructure.adapter.out.persistence.repository.ProductReactiveRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ProductReactiveRepository productReactiveRepository;

	@BeforeEach
	void setUp() {
		productReactiveRepository.deleteAll().block();
	}

	@Test
	void shouldCreateFindUpdateAndDeleteProductLogicallyUsingH2R2dbc() {
		@SuppressWarnings("unchecked")
		Map<String, Object> createdProduct = webTestClient.post()
				.uri("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request("FND100", "Fondo Inicial", "100.50"))
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Map.class)
				.returnResult()
				.getResponseBody();

		assertThat(createdProduct).isNotNull();
		Integer id = (Integer) createdProduct.get("id");
		assertThat(id).isNotNull();
		assertThat(createdProduct.get("code")).isEqualTo("FND100");

		webTestClient.get()
				.uri("/api/products/{id}", id)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.code").isEqualTo("FND100")
				.jsonPath("$.state").isEqualTo(true);

		webTestClient.get()
				.uri("/api/products")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[0].id").isEqualTo(id);

		webTestClient.put()
				.uri("/api/products/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request("FND101", "Fondo Actualizado", "250.75"))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.code").isEqualTo("FND101")
				.jsonPath("$.name").isEqualTo("Fondo Actualizado")
				.jsonPath("$.modDate").isNotEmpty();

		webTestClient.delete()
				.uri("/api/products/{id}", id)
				.exchange()
				.expectStatus().isNoContent();

		webTestClient.get()
				.uri("/api/products/{id}", id)
				.exchange()
				.expectStatus().isNotFound();

		StepVerifier.create(productReactiveRepository.findById(id))
				.assertNext(product -> {
					assertThat(product.getState()).isFalse();
					assertThat(product.getModDate()).isNotNull();
				})
				.verifyComplete();
	}

	private Map<String, Object> request(String code, String name, String price) {
		return Map.of(
				"code", code,
				"name", name,
				"description", "Producto de prueba",
				"price", price,
				"category", "Fondos",
				"state", true
		);
	}

}
