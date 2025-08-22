package com.awesome.pizza.application.data.core;

import com.awesome.pizza.application.data.abstraction.APMessageEnum;

public enum CoreMessage implements APMessageEnum {
	/** Entity already exists */
	ENTITY_ALREADY_EXISTS("Entity already exists"),;

	private String msg;

	CoreMessage(String msg) {
		this.msg = msg;
	} // PizzaOrderMessage

	@Override
	public String getMsg() {
		return msg;
	} // getMsg
} // CoreMessage
