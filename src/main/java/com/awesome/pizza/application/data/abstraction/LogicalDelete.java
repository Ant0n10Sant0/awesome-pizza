package com.awesome.pizza.application.data.abstraction;

/** Interface representing an entity implementing logical deletion. */
public interface LogicalDelete {

	public Boolean getLogDel();

	public APModel setLogDel(Boolean logDel);
}
