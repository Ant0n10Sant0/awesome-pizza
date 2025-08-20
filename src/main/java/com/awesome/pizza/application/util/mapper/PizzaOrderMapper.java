package com.awesome.pizza.application.util.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.awesome.pizza.application.data.abstraction.APMapper;
import com.awesome.pizza.application.dto.PizzaOrderDto;
import com.awesome.pizza.application.model.PizzaOrder;

@Component
public class PizzaOrderMapper extends APMapper<PizzaOrder, PizzaOrderDto> {

	protected PizzaOrderMapper(ModelMapper mapper) {
		super(PizzaOrder.class, PizzaOrderDto.class, mapper);
	} // PizzaOrderMapper

} // PizzaOrderMapper
