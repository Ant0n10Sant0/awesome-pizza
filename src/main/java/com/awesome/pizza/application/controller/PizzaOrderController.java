package com.awesome.pizza.application.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.awesome.pizza.application.dto.PizzaOrderDto;
import com.awesome.pizza.application.dto.PizzaOrderStatusDto;
import com.awesome.pizza.application.mapper.PizzaOrderMapper;
import com.awesome.pizza.application.mapper.PizzaOrderStatusMapper;
import com.awesome.pizza.application.model.PizzaOrder;
import com.awesome.pizza.application.model.PizzaOrderStatus;
import com.awesome.pizza.application.service.pizzaorder.PizzaOrderService;

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
	 * Saves an order and puts it in the order queue. It'll fail if the queue is
	 * full, causing a rollback.
	 */
	@PostMapping(path = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> postOrder(@RequestBody @Valid PizzaOrderDto dto) {
	    PizzaOrder entity = mapper.toEntity(dto);
	    PizzaOrder result = service.insert(entity);
		return ResponseEntity.ok(result.getCode());
	} // postOrder

	/** Invokes {@link PizzaOrderService#getOrderStatusByCode(String)} */
	@GetMapping(path = "/checkStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PizzaOrderStatusDto> checkOrderStatusByCode(@RequestParam("code") String orderCode) {
		PizzaOrderStatus status = service.getOrderStatusByCode(orderCode);
		return ResponseEntity.ok(statusMapper.toDto(status));
	} // checkOrderStatusByCode

	/** Invokes {@link PizzaOrderService#getNewOrders(Pageable)} */
	@GetMapping(path = "/getNewOrders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedModel<PizzaOrderDto>> getNewOrders(Pageable pageable) {
		Page<PizzaOrder> page = service.getNewOrders(pageable);
		Page<PizzaOrderDto> dtoPage = page.map(mapper::toDto);
		return ResponseEntity.ok(new PagedModel<>(dtoPage));
	} // getNewOrders

	/** Invokes {@link PizzaOrderService#takeNextOrder()} */
	@GetMapping(path = "/takeNextOrder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PizzaOrderDto> takeNextOrder() {
		PizzaOrder order = service.takeNextOrder();
		return ResponseEntity.ok(mapper.toDto(order));
	} // takeNextOrder

	/** Invokes {@link PizzaOrderService#closeOrder(String)} */
	@PutMapping(path = "/closeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> closeOrder(@RequestParam("code") String orderCode) {
		service.closeOrder(orderCode);
		return ResponseEntity.ok(null);
	} // closeOrder
} // PizzaOrderController
