package com.awesome.pizza.application.repository;

import com.awesome.pizza.application.data.abstraction.APModelDao;
import com.awesome.pizza.application.data.abstraction.LogicalDeleteDao;
import com.awesome.pizza.application.model.PizzaOrderStatus;

public interface PizzaOrderStatusRepository extends APModelDao<PizzaOrderStatus>, LogicalDeleteDao<PizzaOrderStatus> {

}