package com.awesome.pizza.application.data.exception;

public class APException extends RuntimeException {

	private static final long serialVersionUID = 4430596251157218994L;

	public APException() {
		super();
	}

	public APException(String message) {
		super(message);
	}

	public APException(Throwable cause) {
		super(cause);
	}

}
