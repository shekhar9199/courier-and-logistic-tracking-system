package com.springboot.exception;

public class DetailsAllreadyExistException extends RuntimeException {
	public DetailsAllreadyExistException(String message) {
		super(message);
	}
}
