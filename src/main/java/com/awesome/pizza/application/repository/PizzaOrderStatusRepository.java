package com.awesome.pizza.application.repository;

import java.util.Optional;

import com.awesome.pizza.application.data.abstraction.APModelDao;
import com.awesome.pizza.application.data.abstraction.LogicalDeleteDao;
import com.awesome.pizza.application.model.PizzaOrderStatus;

public interface PizzaOrderStatusRepository extends APModelDao<PizzaOrderStatus>, LogicalDeleteDao<PizzaOrderStatus> {

	public default Optional<PizzaOrderStatus> findByCode(String code) {
		return findByLogDelIsFalseAndCode(code);
	}

	public Optional<PizzaOrderStatus> findByLogDelIsFalseAndCode(String code);

}