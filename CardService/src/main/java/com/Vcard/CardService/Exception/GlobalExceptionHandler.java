package com.Vcard.CardService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Vcard.CardService.*;



@ControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(CustomRunTimeException.class)
	    public ResponseEntity<String> handleCustomException(CustomRunTimeException ex) {
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }

	 @ExceptionHandler(CustomIllegalArguementException.class)
	 public ResponseEntity<String> handleCustomException(CustomIllegalArguementException ex){
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	 }
 // Add more exception handlers as needed
	 
	 @ExceptionHandler(Exception.class)
	 public ResponseEntity<String> handleGenericException(Exception ex) {
	     
	     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
	 }
    // Add more exception handlers as needed
}
