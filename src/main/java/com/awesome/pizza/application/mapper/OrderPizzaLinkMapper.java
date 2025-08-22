package com.awesome.pizza.application.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.awesome.pizza.application.data.abstraction.APMapper;
import com.awesome.pizza.application.dto.OrderPizzaLinkDto;
import com.awesome.pizza.application.model.OrderPizzaLink;
import com.awesome.pizza.application.model.Pizza;
import com.awesome.pizza.application.repository.PizzaRepository;

@Component
public class OrderPizzaLinkMapper extends APMapper<OrderPizzaLink, OrderPizzaLinkDto> {

	private final PizzaRepository pizzaRepository;

	protected OrderPizzaLinkMapper(ModelMapper mapper, PizzaRepository pizzaRepository) {
		super(OrderPizzaLink.class, OrderPizzaLinkDto.class, mapper);
		this.pizzaRepository = pizzaRepository;

		addDtoToEntityConversion(this::getPizzaId, OrderPizzaLink::setPizzaId);
		addEntityToDtoConversion(this::getPizzaCode, OrderPizzaLinkDto::setPizza);
	} // OrderPizzaLinkMapper

	public Long getPizzaId(OrderPizzaLinkDto dto) {
		if (dto == null || !StringUtils.hasLength(dto.getPizza())) {
			return null;
		}
		String pizza = dto.getPizza();
		return pizzaRepository.findByCode(pizza).map(Pizza::getId).orElse(null);
	} // getPizzaCode

	public String getPizzaCode(OrderPizzaLink source) {
		if (source == null) { return null; }
		long pizzaId = source.getPizzaId();
		return pizzaRepository.findById(pizzaId).map(Pizza::getCode).orElse(null);
	} // getPizzaCode
} // OrderPizzaLinkMapper
