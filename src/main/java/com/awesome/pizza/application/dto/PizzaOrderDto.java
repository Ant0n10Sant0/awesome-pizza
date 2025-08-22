package com.awesome.pizza.application.dto;

import java.util.Objects;
import java.util.Set;

import jakarta.validation.Valid;

public class PizzaOrderDto {
	private Long id;
	private String code;
	private String status;
	private Integer tableNr;
	private String deliveryCity;
	private String deliveryStreet;
	private String deliveryZip;
	@Valid
	private Set<OrderPizzaLinkDto> orderLinks;

	public Long getId() {
		return id;
	} // getId

	public PizzaOrderDto setId(Long id) {
		this.id = id;
		return this;
	} // setId

	public String getCode() {
		return code;
	} // getCode

	public PizzaOrderDto setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public String getStatus() {
		return status;
	} // getStatus

	public PizzaOrderDto setStatus(String status) {
		this.status = status;
		return this;
	} // setStatus

	public Integer getTableNr() {
		return tableNr;
	} // getTableNr

	public PizzaOrderDto setTableNr(Integer tableNr) {
		this.tableNr = tableNr;
		return this;
	} // setTableNr

	public String getDeliveryCity() {
		return deliveryCity;
	} // getDeliveryCity

	public PizzaOrderDto setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
		return this;
	} // setDeliveryCity

	public String getDeliveryStreet() {
		return deliveryStreet;
	} // getDeliveryStreet

	public PizzaOrderDto setDeliveryStreet(String deliveryStreet) {
		this.deliveryStreet = deliveryStreet;
		return this;
	} // setDeliveryStreet

	public String getDeliveryZip() {
		return deliveryZip;
	} // getDeliveryZip

	public PizzaOrderDto setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
		return this;
	} // setDeliveryZip

	public Set<OrderPizzaLinkDto> getOrderLinks() {
		return orderLinks;
	} // getOrderLinks

	public PizzaOrderDto setOrderLinks(Set<OrderPizzaLinkDto> orderLinks) {
		this.orderLinks = orderLinks;
		return this;
	} // setOrderLinks

	@Override
	public int hashCode() {
		return Objects.hash(code, deliveryCity, deliveryStreet, deliveryZip, id, status, tableNr);
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
		return Objects.equals(code, other.code) && Objects.equals(deliveryCity, other.deliveryCity)
				&& Objects.equals(deliveryStreet, other.deliveryStreet)
				&& Objects.equals(deliveryZip, other.deliveryZip) && Objects.equals(id, other.id)
				&& Objects.equals(status, other.status) && Objects.equals(tableNr, other.tableNr);
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaOrderDto [id=").append(id).append(", code=").append(code).append(", status=")
				.append(status).append(", tableNr=").append(tableNr).append(", deliveryCity=").append(deliveryCity)
				.append(", deliveryStreet=").append(deliveryStreet).append(", deliveryZip=").append(deliveryZip)
				.append(", orderLinks=").append(orderLinks).append("]");
		return builder.toString();
	} // toString

} // PizzaOrderDto
