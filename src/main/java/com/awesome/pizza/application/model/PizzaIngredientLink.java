package com.awesome.pizza.application.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.awesome.pizza.application.data.abstraction.APModel;

@Table
public class PizzaIngredientLink implements APModel {
	@Id
	private long id;
	private long ingredientId;

	@Override
	public long getId() {
		return id;
	} // getId

	@Override
	public PizzaIngredientLink setId(long id) {
		this.id = id;
		return this;
	} // setId

	public long getIngredientId() {
		return ingredientId;
	} // getIngredientId

	public PizzaIngredientLink setIngredientId(long pizzaId) {
		this.ingredientId = pizzaId;
		return this;
	} // setIngredientId

	@Override
	public int hashCode() {
		return Objects.hash(id, ingredientId);
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
		PizzaIngredientLink other = (PizzaIngredientLink) obj;
		return id == other.id && ingredientId == other.ingredientId;
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaIngredientLink [id=").append(id).append(", ingredientId=").append(ingredientId)
				.append("]");
		return builder.toString();
	} // toString

} // PizzaIngredientLink
