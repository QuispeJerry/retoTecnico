package com.example.demo.shared.exception;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(Integer id) {
		super("Product with id " + id + " was not found");
	}

}
