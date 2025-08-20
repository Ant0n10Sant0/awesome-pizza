package com.awesome.pizza.application.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import com.awesome.pizza.application.data.abstraction.APAuditModel;
import com.awesome.pizza.application.data.abstraction.LogicalDelete;

@Table
public class PizzaOrderStatus implements APAuditModel, LogicalDelete {

	public static final Long NEW = 1L;
	public static final Long IN_PROGRESS = 2L;
	public static final Long READY = 3L;

	@Id
	private long id;
	private String code;
	private String descr;
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
	public PizzaOrderStatus setId(long id) {
		this.id = id;
		return this;
	} // setId

	public String getCode() {
		return code;
	} // getCode

	public PizzaOrderStatus setCode(String code) {
		this.code = code;
		return this;
	} // setCode

	public String getDescr() {
		return descr;
	} // getDescr

	public PizzaOrderStatus setDescr(String descr) {
		this.descr = descr;
		return this;
	} // setDescr

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
	public PizzaOrderStatus setLogDel(Boolean logDel) {
		this.logDel = logDel;
		return this;
	} // setLogDel


	@Override
	public int hashCode() {
		return Objects.hash(code, descr, id);
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
		PizzaOrderStatus other = (PizzaOrderStatus) obj;
		return Objects.equals(code, other.code) && Objects.equals(descr, other.descr) && id == other.id;
	} // equals

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PizzaOrderStatus [id=").append(id).append(", code=").append(code).append(", descr=")
				.append(descr).append(", recVer=").append(recVer).append(", tsi=").append(tsi).append(", tsu=")
				.append(tsu).append(", logDel=").append(logDel).append("]");
		return builder.toString();
	} // toString

	public enum PizzaOrderStatusEnum {
		NULL(null),
		NEW(PizzaOrderStatus.NEW),
		IN_PROGRESS(PizzaOrderStatus.IN_PROGRESS),
		READY(PizzaOrderStatus.READY);

		PizzaOrderStatusEnum(Long id) {
			this.id = id;
		} // PizzaOrderStatusEnum

		private Long id;

		public Long getId() {
			return id;
		} // getId

		public static final Map<Long, PizzaOrderStatusEnum> VALUES = new HashMap<>();
		static {
			for (PizzaOrderStatusEnum val : values()) {
				VALUES.put(val.getId(), val);
			}
		}

		public static PizzaOrderStatusEnum getEnum(Long id) {
			return VALUES.get(id);
		} // getEnum

	} // PizzaOrderStatusEnum
} // PizzaOrderStatus
