package com.awesome.pizza.application.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class APNotFoundException extends APException {
	private static final long serialVersionUID = -7112712135167238099L;

	public APNotFoundException() {
		super();
	}

	public APNotFoundException(String message) {
		super(message);
	}

}
