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
import com.awesome.pizza.application.data.abstraction.LogicalDelete;

@Table
public class Pizza implements APAuditModel, LogicalDelete {

	@Id
	private long id;
	private String code;
	private String descr;
	private long baseId;
	@Version
	private Integer recVer;
	@CreatedDate
	private LocalDateTime tsi;
	@LastModifiedDate
	private LocalDateTime tsu;
	private Boolean logDel = false;

	@MappedCollection(idColumn = "pizza_id")
	Set<PizzaIngredientLink> ingredientLinks;

	@Override
	public long getId() {
		return id;
	} // getId

	@Override
	public Pizza setId(long id) {
		this.id = id;
		return this;
	} // setId

	public String getCode() {
		return code;
	} // getCode

	public Pizza setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public String getDescr() {
		return descr;
	} // getDescr

	public Pizza setDescr(String descr) {
		this.descr = descr;
		return this;
	} // setDescr

	public long getBaseId() {
		return baseId;
	} // getBaseId

	public Pizza setBaseId(long baseId) {
		this.baseId = baseId;
		return this;
	} // setBaseId

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

	@Override
	public Boolean getLogDel() {
		return logDel;
	} // getLogDel

	@Override
	public Pizza setLogDel(Boolean logDel) {
		this.logDel = logDel;
		return this;
	} // setLogDel

	public Set<PizzaIngredientLink> getIngredientLinks() {
		return ingredientLinks;
	} // getIngredientLinks

	public Pizza setIngredientLinks(Set<PizzaIngredientLink> ingredientLinks) {
		this.ingredientLinks = ingredientLinks;
		return this;
	} // setIngredientLinks

	public Pizza addIngredientLink(PizzaIngredientLink ingredientLink) {
		if (ingredientLinks == null) {
			ingredientLinks = new HashSet<>();
		}
		ingredientLinks.add(ingredientLink);
		return this;
	} // addIngredientLink

	@Override
	public int hashCode() {
		return Objects.hash(baseId, code, descr, id);
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
		Pizza other = (Pizza) obj;
		return baseId == other.baseId && Objects.equals(code, other.code) && Objects.equals(descr, other.descr)
				&& id == other.id;
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pizza [id=").append(id).append(", code=").append(code).append(", descr=").append(descr)
				.append(", baseId=").append(baseId).append(", recVer=").append(recVer).append(", tsi=").append(tsi)
				.append(", tsu=").append(tsu).append(", logDel=").append(logDel).append("]");
		return builder.toString();
	} // toString

} // Pizza
