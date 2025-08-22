package com.awesome.pizza.application.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import com.awesome.pizza.application.data.abstraction.APAuditModel;

@Table
public class PizzaOrder implements APAuditModel {
	@Id
	private long id;
	private String code;
	private long statusId;
	private Integer tableNr;
	private String deliveryCity;
	private String deliveryStreet;
	private String deliveryZip;
	@Version
	private Integer recVer;
	@CreatedDate
	private LocalDateTime tsi;
	@LastModifiedDate
	private LocalDateTime tsu;

	@MappedCollection(idColumn = "order_id")
	private Set<OrderPizzaLink> orderLinks;

	@Override
	public long getId() {
		return id;
	} // getId

	@Override
	public PizzaOrder setId(long id) {
		this.id = id;
		return this;
	} // setId

	public String getCode() {
		return code;
	} // getCode

	public PizzaOrder setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public long getStatusId() {
		return statusId;
	} // getStatusId

	public PizzaOrder setStatusId(long statusId) {
		this.statusId = statusId;
		return this;
	} // setStatusId

	public Integer getTableNr() {
		return tableNr;
	} // getTableNr

	public PizzaOrder setTableNr(Integer tableNr) {
		this.tableNr = tableNr;
		return this;
	} // setTableNr

	public String getDeliveryCity() {
		return deliveryCity;
	} // getDeliveryCity

	public PizzaOrder setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
		return this;
	} // setDeliveryCity

	public String getDeliveryStreet() {
		return deliveryStreet;
	} // getDeliveryStreet

	public PizzaOrder setDeliveryStreet(String deliveryStreet) {
		this.deliveryStreet = deliveryStreet;
		return this;
	} // setDeliveryStreet

	public String getDeliveryZip() {
		return deliveryZip;
	} // getDeliveryZip

	public PizzaOrder setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
		return this;
	} // setDeliveryZip

	@Override
	public LocalDateTime getTsi() {
		return tsi;
	} // getTsi

	@Override
	public LocalDateTime getTsu() {
		return tsu;
	} // getTsu

	@Override
	public Integer getRecVer() {
		return recVer;
	} // getRecVer

	public Set<OrderPizzaLink> getOrderLinks() {
		return orderLinks;
	} // getOrderLinks

	public PizzaOrder setOrderLinks(Set<OrderPizzaLink> orderLinks) {
		this.orderLinks = orderLinks;
		return this;
	} // setOrderLinks

	public PizzaOrder addOrderLink(OrderPizzaLink orderLink) {
		if (orderLinks == null) {
			orderLinks = new HashSet<>();
		}
		orderLinks.add(orderLink);
		return this;
	} // addOrderLink

	@Override
	public int hashCode() {
		return Objects.hash(code, deliveryCity, deliveryStreet, deliveryZip, id, orderLinks, statusId, tableNr);
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
		PizzaOrder other = (PizzaOrder) obj;
		return Objects.equals(code, other.code) && Objects.equals(deliveryCity, other.deliveryCity)
				&& Objects.equals(deliveryStreet, other.deliveryStreet)
				&& Objects.equals(deliveryZip, other.deliveryZip) && id == other.id
				&& statusId == other.statusId && Objects.equals(tableNr, other.tableNr);
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaOrder [id=").append(id).append(", code=").append(code).append(", statusId=")
				.append(statusId).append(", tableNr=").append(tableNr).append(", deliveryCity=").append(deliveryCity)
				.append(", deliveryStreet=").append(deliveryStreet).append(", deliveryZip=").append(deliveryZip)
				.append(", recVer=").append(recVer).append(", tsi=").append(tsi).append(", tsu=").append(tsu)
				.append(", orderLinks=").append(orderLinks).append("]");
		return builder.toString();
	} // toString
	
} // PizzaOrder
