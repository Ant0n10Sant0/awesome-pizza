package com.awesome.pizza.application.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.awesome.pizza.application.data.abstraction.APModelDao;
import com.awesome.pizza.application.model.PizzaOrder;

public interface PizzaOrderRepository extends APModelDao<PizzaOrder> {

	public Optional<PizzaOrder> findByCode(String code);

	public List<PizzaOrder> findAllByStatusIdOrderByTsuAsc(Long statusId);

	public List<PizzaOrder> findByStatusIdAndTsuGreaterThanEqualAndTsuBeforeOrderByTsuAsc(Long statusId,
			LocalDateTime start, LocalDateTime end);
	
}
