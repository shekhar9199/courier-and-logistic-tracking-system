package com.springboot.exception;

public class MethodArgumentTypeMismatchException extends RuntimeException {
	
	public MethodArgumentTypeMismatchException(String message) {
		super(message);
	}
}
