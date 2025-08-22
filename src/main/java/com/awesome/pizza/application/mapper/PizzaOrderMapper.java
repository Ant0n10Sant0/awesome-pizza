package com.awesome.pizza.application.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.awesome.pizza.application.data.abstraction.APMapper;
import com.awesome.pizza.application.dto.PizzaOrderDto;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.repository.PizzaOrderStatusRepository;

@Component
public class PizzaOrderMapper extends APMapper<PizzaOrder, PizzaOrderDto> {
	private final PizzaOrderStatusRepository statusRepository;

	protected PizzaOrderMapper(ModelMapper mapper, PizzaOrderStatusRepository statusRepository) {
		super(PizzaOrder.class, PizzaOrderDto.class, mapper);
		this.statusRepository = statusRepository;

		addEntityToDtoConversion(this::getStatusCode, PizzaOrderDto::setStatus);
		addDtoToEntityConversion(this::getStatusId, PizzaOrder::setStatusId);
	} // PizzaOrderMapper

	public String getStatusCode(PizzaOrder source) {
		if (source == null) { return null; }
		long statusId = source.getStatusId();
		return statusRepository.findById(statusId).map(PizzaOrderStatus::getCode).orElse(null);
	} // getPizzaCode

	public Long getStatusId(PizzaOrderDto source) {
		if (source == null) {
			return null;
		}
		String code = source.getStatus();
		return statusRepository.findByCode(code).map(PizzaOrderStatus::getId).orElse(null);
	} // getPizzaCode
} // PizzaOrderMapper
