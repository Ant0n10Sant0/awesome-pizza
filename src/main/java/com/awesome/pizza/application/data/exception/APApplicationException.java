package com.awesome.pizza.application.data.exception;

import com.awesome.pizza.application.data.abstraction.APMessageEnum;

public class APApplicationException extends APException {

	private static final long serialVersionUID = -1500855542775395287L;

	public APApplicationException(APMessageEnum messageEnum) {
		this(messageEnum.getMsg());
	}

	public APApplicationException(String message) {
		super(message);
	}
}
