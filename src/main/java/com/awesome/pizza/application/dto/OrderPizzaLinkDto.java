package com.awesome.pizza.application.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;

public class OrderPizzaLinkDto {
	private String extra;
	@NotBlank
	private String pizza;

	public String getExtra() {
		return extra;
	} // getExtra

	public OrderPizzaLinkDto setExtra(String extra) {
		this.extra = extra;
		return this;
	} // setExtra

	public String getPizza() {
		return pizza;
	} // getPizza

	public OrderPizzaLinkDto setPizza(String pizza) {
		this.pizza = pizza;
		return this;
	} // setPizza

	@Override
	public int hashCode() {
		return Objects.hash(extra, pizza);
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
		OrderPizzaLinkDto other = (OrderPizzaLinkDto) obj;
		return Objects.equals(extra, other.extra) && Objects.equals(pizza, other.pizza);
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderPizzaLinkDto [extra=").append(extra).append(", pizza=").append(pizza).append("]");
		return builder.toString();
	} // toString

} // OrderPizzaLinkDto
