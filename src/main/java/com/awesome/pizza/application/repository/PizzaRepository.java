package com.awesome.pizza.application.repository;

import java.util.Optional;

import com.awesome.pizza.application.data.abstraction.APModelDao;
import com.awesome.pizza.application.data.abstraction.LogicalDeleteDao;
import com.awesome.pizza.application.model.Pizza;

public interface PizzaRepository extends APModelDao<Pizza>, LogicalDeleteDao<Pizza> {

    public Optional<Pizza> findByCode(String code);

}
