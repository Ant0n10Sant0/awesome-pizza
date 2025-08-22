package com.awesome.pizza.application.service.pizzaorder;

import com.awesome.pizza.application.data.abstraction.APMessageEnum;

public enum PizzaOrderMessage implements APMessageEnum {
	/** Invalid pizza order status */
	INVALID_ORDER_STATUS("Invalid pizza order status"),;

	private String msg;

	PizzaOrderMessage(String msg) {
		this.msg = msg;
	} // PizzaOrderMessage

	@Override
	public String getMsg() {
		return msg;
	} // getMsg
} // PizzaOrderMessage
