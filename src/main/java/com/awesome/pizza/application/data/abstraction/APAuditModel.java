package com.awesome.pizza.application.data.abstraction;

import java.time.LocalDateTime;

public interface APAuditModel extends APModel {

	public Integer getRecVer();

	public LocalDateTime getTsi();

	public LocalDateTime getTsu();
}
