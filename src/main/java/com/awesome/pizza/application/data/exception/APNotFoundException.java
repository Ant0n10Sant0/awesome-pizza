package com.awesome.pizza.application.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = APNotFoundException.MESSAGE)
public class APNotFoundException extends APException {
	private static final long serialVersionUID = -7112712135167238099L;
	private static final String MESSAGE = "Entity not found";

	public APNotFoundException() {
		super(APNotFoundException.MESSAGE);
	}

}
