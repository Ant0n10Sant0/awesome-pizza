package com.awesome.pizza.application.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.awesome.pizza.application.data.abstraction.APModel;

@Table
public class OrderPizzaLink implements APModel {

	@Id
	private long id;
	private long pizzaId;
	private String extra;

	@Override
	public long getId() {
		return id;
	} // getId

	@Override
	public OrderPizzaLink setId(long id) {
		this.id = id;
		return this;
	} // setId

	public long getPizzaId() {
		return pizzaId;
	} // getPizzaId

	public OrderPizzaLink setPizzaId(long pizzaId) {
		this.pizzaId = pizzaId;
		return this;
	} // setPizzaId

	public String getExtra() {
		return extra;
	} // getCode

	public OrderPizzaLink setExtra(String extra) {
		this.extra = extra;
		return this;
	} // setCode

	@Override
	public int hashCode() {
		return Objects.hash(extra, id, pizzaId);
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
		OrderPizzaLink other = (OrderPizzaLink) obj;
		return Objects.equals(extra, other.extra) && id == other.id && pizzaId == other.pizzaId;
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderPizzaLink [id=").append(id).append(", pizzaId=").append(pizzaId).append(", extra=")
				.append(extra).append("]");
		return builder.toString();
	} // toString

} // OrderPizzaLink
