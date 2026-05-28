package com.example.demo.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public class ProductEntity {

	@Id
	private Integer id;

	private String code;

	private String name;

	private String description;

	private BigDecimal price;

	private String category;

	@Column("reg_date")
	private LocalDateTime regDate;

	@Column("mod_date")
	private LocalDateTime modDate;

	private Boolean state;

	public ProductEntity() {
	}

	public ProductEntity(
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
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.price = price;
		this.category = category;
		this.regDate = regDate;
		this.modDate = modDate;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getModDate() {
		return modDate;
	}

	public void setModDate(LocalDateTime modDate) {
		this.modDate = modDate;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

}
