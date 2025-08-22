package com.awesome.pizza.application.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.awesome.pizza.application.data.abstraction.APMapper;
import com.awesome.pizza.application.dto.PizzaOrderStatusDto;
import com.awesome.pizza.application.model.PizzaOrderStatus;

@Component
public class PizzaOrderStatusMapper extends APMapper<PizzaOrderStatus, PizzaOrderStatusDto> {

	protected PizzaOrderStatusMapper(ModelMapper mapper) {
		super(PizzaOrderStatus.class, PizzaOrderStatusDto.class, mapper);
	} // PizzaOrderStatusMapper
} // PizzaOrderStatusMapper
