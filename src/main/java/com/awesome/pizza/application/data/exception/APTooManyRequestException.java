package com.awesome.pizza.application.data.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "Too many requests, retry at a later time")
public class APTooManyRequestException extends APException {

	private static final long serialVersionUID = -2050273416085008582L;

	public APTooManyRequestException() {
		super();
	}

	public APTooManyRequestException(String message) {
		super(message);
	}
}
