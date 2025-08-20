package com.awesome.pizza.application.dto;

import java.util.Objects;

public class PizzaOrderStatusDto {
	private String code;
	private String descr;

	public String getCode() {
		return code;
	} // getCode

	public PizzaOrderStatusDto setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public String getDescr() {
		return descr;
	} // getDescr

	public PizzaOrderStatusDto setDescr(String descr) {
		this.descr = descr;
		return this;
	} // setDescr

	@Override
	public int hashCode() {
		return Objects.hash(code, descr);
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
		PizzaOrderStatusDto other = (PizzaOrderStatusDto) obj;
		return Objects.equals(code, other.code) && Objects.equals(descr, other.descr);
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaOrderStatusDto [code=").append(code).append(", descr=").append(descr).append("]");
		return builder.toString();
	} // toString

} // PizzaOrderStatusDto
