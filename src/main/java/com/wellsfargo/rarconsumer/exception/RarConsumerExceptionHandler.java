package com.wellsfargo.rarconsumer.exception;

import com.wellsfargo.rarconsumer.response.*;
import com.wellsfargo.rarconsumer.util.ResponseUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class RarConsumerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResponseApiDTO> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    return ResponseUtility.createUserErrorResponse(ex.getUserMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ResponseApiDTO> InternalServerExceptionHandler(InternalServerException ex, WebRequest request) {
    return ResponseUtility.createSystemErrorReponse(ex.getSystemErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ResponseApiDTO> badRequestExceptionHandler(BadRequestException ex, WebRequest request) {
    return ResponseUtility.createUserErrorResponse(ex.getUserMessage(), HttpStatus.BAD_REQUEST);
  }

}
