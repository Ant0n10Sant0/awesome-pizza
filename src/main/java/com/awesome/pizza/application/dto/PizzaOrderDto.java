package com.awesome.pizza.application.dto;

import java.util.Objects;
import java.util.Set;

import jakarta.validation.Valid;

public class PizzaOrderDto {
	private String code;
	@Valid
	private Set<OrderPizzaLinkDto> orderLinks;

	public String getCode() {
		return code;
	} // getCode

	public PizzaOrderDto setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public Set<OrderPizzaLinkDto> getOrderLinks() {
		return orderLinks;
	} // getOrderLinks

	public PizzaOrderDto setOrderLinks(Set<OrderPizzaLinkDto> orderLinks) {
		this.orderLinks = orderLinks;
		return this;
	} // setOrderLinks

	@Override
	public int hashCode() {
		return Objects.hash(code, orderLinks);
	} // hashCode

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PizzaOrderDto other = (PizzaOrderDto) obj;
		return Objects.equals(code, other.code) && Objects.equals(orderLinks, other.orderLinks);
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaOrderDto [code=").append(code).append(", orderLinks=").append(orderLinks).append("]");
		return builder.toString();
	} // toString

} // PizzaOrderDto
