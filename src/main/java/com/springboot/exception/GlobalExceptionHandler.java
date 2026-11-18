package com.springboot.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.springboot.dto.ResponseStructure;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(IdNotFoundException.class)
		public ResponseEntity<ResponseStructure<String>> HandleDataIntegrityViolation(IdNotFoundException exception){
		
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("No Record Found");
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(NoRecordAvailableException.class)
	public ResponseEntity<ResponseStructure<String>> HandleNoRecordAvailableException(NoRecordAvailableException exception){
	
	ResponseStructure<String> res = new ResponseStructure<String>();
	res.setStatusCode(HttpStatus.NOT_FOUND.value());
	res.setMessage(exception.getMessage());
	res.setData("No Record Found");
	return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DetailsAllreadyExistException.class)
	public ResponseEntity<ResponseStructure<String>>
	handleDetailsAlreadyExistException(
	        DetailsAllreadyExistException exception) {

	    ResponseStructure<String> response = new ResponseStructure<>();

	    response.setStatusCode(HttpStatus.CONFLICT.value());
	    response.setMessage(exception.getMessage());
	    response.setData("Duplicate record not allowed");

	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ResponseStructure<String>> handleIllegalStateException(
	        IllegalStateException exception) {

	    ResponseStructure<String> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.CONFLICT.value());
	    response.setMessage(exception.getMessage());
	    response.setData("Operation not allowed");

	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseStructure<String>> handleTypeMismatch(
	        MethodArgumentTypeMismatchException exception) {

	    String message = "Invalid value: " + exception.getValue();

	    Class<?> requiredType = exception.getRequiredType();

	    if (requiredType != null && requiredType.isEnum()) {
	        String allowedValues = Arrays.stream(requiredType.getEnumConstants())
	                .map(Object::toString)
	                .collect(Collectors.joining(", "));

	        message += ". Allowed values: " + allowedValues;
	    }

	    ResponseStructure<String> response = new ResponseStructure<>();
	    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
	    response.setMessage(message);
	    response.setData(null);

	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PaymentAlreadyUsedException.class)
    public ResponseEntity<String> handlePaymentAlreadyUsed(
            PaymentAlreadyUsedException ex) {

        return new ResponseEntity<>(
            ex.getMessage(),
            HttpStatus.CONFLICT
        );
    }
}
