package com.awesome.pizza.application.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import com.awesome.pizza.application.data.abstraction.APAuditModel;
import com.awesome.pizza.application.data.abstraction.LogicalDelete;

@Table
public class IngredientType implements APAuditModel, LogicalDelete {

	@Id
	private long id;
	private String code;
	@Version
	private Integer recVer;
	@CreatedDate
	private LocalDateTime tsi;
	@LastModifiedDate
	private LocalDateTime tsu;
	private Boolean logDel = false;

	@Override
	public long getId() {
		return id;
	} // getId

	@Override
	public IngredientType setId(long id) {
		this.id = id;
		return this;
	} // setId

	public String getCode() {
		return code;
	} // getCode

	public IngredientType setCode(String code) {
		this.code = code;
		return this;
	} // setCode

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
	public IngredientType setLogDel(Boolean logDel) {
		this.logDel = logDel;
		return this;
	} // setLogDel

	@Override
	public int hashCode() {
		return Objects.hash(code, id);
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
		IngredientType other = (IngredientType) obj;
		return Objects.equals(code, other.code) && id == other.id;
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IngredientType [id=").append(id).append(", code=").append(code).append(", recVer=")
				.append(recVer).append(", tsi=").append(tsi).append(", tsu=").append(tsu).append(", logDel=")
				.append(logDel).append("]");
		return builder.toString();
	} // toString

} // IngredientType
