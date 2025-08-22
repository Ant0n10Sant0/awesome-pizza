package com.awesome.pizza.application.data.abstraction;

import java.util.Optional;

/**
 * Interface representing a generic repository for a {@link APModel}
 * implementing logical deletion.
 */
public interface LogicalDeleteDao<T extends APModel & LogicalDelete> {
	Optional<T> findByLogDelIsFalseAndId(Long id);
}
