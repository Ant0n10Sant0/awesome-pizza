package com.awesome.pizza.application.data.abstraction;

import java.time.LocalDateTime;

/**
 * Interface extending {@link APModel} and representing an auditable entity,
 * with versioning.
 */
public interface APAuditModel extends APModel {

	/**
	 * Gets the record version.
	 */
	public Integer getRecVer();

	/**
	 * Gets the insertion timestamp.
	 */
	public LocalDateTime getTsi();

	/**
	 * Gets the last updated timestamp.
	 */
	public LocalDateTime getTsu();
}
