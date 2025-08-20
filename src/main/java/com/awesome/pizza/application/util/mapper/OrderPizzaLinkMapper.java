package com.awesome.pizza.application.util.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider.ProvisionRequest;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.awesome.pizza.application.data.abstraction.APMapper;
import com.awesome.pizza.application.data.exception.APNotFoundException;
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

		TypeMap<OrderPizzaLinkDto, OrderPizzaLink> dtoToEntityMap = mapper.createTypeMap(OrderPizzaLinkDto.class,
				OrderPizzaLink.class);
		dtoToEntityMap.setProvider(this::provideEntity);

		TypeMap<OrderPizzaLink, OrderPizzaLinkDto> entityToDtoTypeMap = mapper.createTypeMap(OrderPizzaLink.class,
				OrderPizzaLinkDto.class);
		entityToDtoTypeMap.addMappings(expMap -> expMap.using(ctx -> {
					OrderPizzaLink link = (OrderPizzaLink) ctx.getSource();
					return this.getPizzaCode(link);
				}).map(src -> src, OrderPizzaLinkDto::setPizza));
	} // OrderPizzaLinkMapper

	public OrderPizzaLink provideEntity(ProvisionRequest<OrderPizzaLink> req) {
		OrderPizzaLinkDto dto = (OrderPizzaLinkDto) req.getSource();
		OrderPizzaLink entity = new OrderPizzaLink();
		
		if (dto != null && StringUtils.hasLength(dto.getPizza())) {
			// This lets me link the code with the id during the conversion
			Long pizzaId = pizzaRepository.findByCode(dto.getPizza()).map(Pizza::getId)
					.orElseThrow(() -> new APNotFoundException("Pizza not found: " + dto.getPizza()));
			entity.setPizzaId(pizzaId);
		} // if

		return entity;
	} // provideEntity

	public String getPizzaCode(OrderPizzaLink source) {
		if (source == null) { return null; }
		long pizzaId = source.getPizzaId();
		return pizzaRepository.findById(pizzaId).map(Pizza::getCode).orElse(null);
	} // getPizzaCode
} // OrderPizzaLinkMapper
