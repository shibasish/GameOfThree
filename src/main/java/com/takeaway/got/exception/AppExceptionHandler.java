package com.takeaway.got.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
	
	@ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {
        ErrorResponse errorRes = new ErrorResponse();

        errorRes.setTitle("Resource not found");
        errorRes.setDescription(exception.getMessage());
        errorRes.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorRes, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ValueMisingException exception) {
        ErrorResponse errorRes = new ErrorResponse();

        errorRes.setTitle("Invalid request");
        errorRes.setDescription(exception.getMessage());
        errorRes.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
    }
	
//	@ExceptionHandler
//    public ResponseEntity<ExchangeErrorResponse> handleException(Exception exception){
//        ExchangeErrorResponse error = new ExchangeErrorResponse();
//
//        error.setStatus(HttpStatus.BAD_REQUEST.value());
//        error.setTitle("Unsupported request");
//        error.setDescription(exception.getMessage());
//        error.setTimeStamp(System.currentTimeMillis());
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
}
