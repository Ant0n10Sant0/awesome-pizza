package com.awesome.pizza.application.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.awesome.pizza.application.dto.PizzaOrderDto;
import com.awesome.pizza.application.dto.PizzaOrderStatusDto;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.service.pizzaorder.PizzaOrderService;
import com.awesome.pizza.application.util.mapper.PizzaOrderMapper;
import com.awesome.pizza.application.util.mapper.PizzaOrderStatusMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/order")
public class PizzaOrderController {

	private final PizzaOrderService service;
	private final PizzaOrderMapper mapper;
	private final PizzaOrderStatusMapper statusMapper;

	public PizzaOrderController(PizzaOrderService service, PizzaOrderMapper mapper,
			PizzaOrderStatusMapper statusMapper) {
		this.service = service;
	    this.mapper = mapper;
		this.statusMapper = statusMapper;
	} // PizzaOrderController

	/**
	 * Save an order and put it in the order queue. It'll fail if the queue is full.
	 */
	@PostMapping(path = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postOrder(@RequestBody @Valid PizzaOrderDto dto) {
	    PizzaOrder entity = mapper.toEntity(dto);
	    PizzaOrder result = service.insert(entity);
		return ResponseEntity.ok(result.getCode());
	} // postOrder

	@GetMapping(path = "/checkStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PizzaOrderStatusDto> checkOrderStatusByCode(@RequestParam("code") String orderCode) {
		PizzaOrderStatus status = service.getOrderStatusByCode(orderCode);
		return ResponseEntity.ok(statusMapper.toDto(status));
	} // checkOrderStatusByCode

	@GetMapping(path = "/getNewOrders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PizzaOrderDto>> getNewOrders() {
		List<PizzaOrder> newOrders = service.getNewOrders();
		return ResponseEntity.ok(mapper.toDtoList(newOrders));
	} // checkOrderStatusByCode

	@GetMapping(path = "/takeNewOrder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PizzaOrderDto> takeNewOrder() {
		PizzaOrder order = service.takeOrder();
		return ResponseEntity.ok(mapper.toDto(order));
	} // checkOrderStatusByCode
} // PizzaOrderController
