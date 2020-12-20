package com.takeaway.got.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValueMisingException extends RuntimeException {

	public ValueMisingException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValueMisingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ValueMisingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ValueMisingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ValueMisingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	

}
