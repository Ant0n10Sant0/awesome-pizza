package com.awesome.pizza.application.data.abstraction;

import java.util.Optional;

public interface LogicalDeleteDao<T extends APModel & LogicalDelete> {
	Optional<T> findByLogDelIsFalseAndId(Long id);
}
