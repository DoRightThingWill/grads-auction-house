package com.weareadaptive.auction.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import com.weareadaptive.auction.model.KeyAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);

    var invalidFields = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new InvalidField(error.getField(), error.getDefaultMessage())).toList();

    return new ResponseEntity<>(new BadRequestInvalidFieldsProblem(invalidFields), headers,
        BAD_REQUEST);
  }

  @ExceptionHandler(KeyAlreadyExistsException.class)
  public ResponseEntity<Object> handleNotFoundException(KeyAlreadyExistsException ex) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);
    return new ResponseEntity<>(
        new Problem(BAD_REQUEST.value(), BAD_REQUEST.name(), ex.getMessage()), headers,
        BAD_REQUEST);
  }

  @ExceptionHandler(ModelNotFoundException.class)
  public ResponseEntity<Object> handldeModelNotFoundException(ModelNotFoundException exception) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);
    return new ResponseEntity<>(
        new Problem(NOT_FOUND.value(), NOT_FOUND.name(), exception.getMessage()), headers,
        NOT_FOUND);
  }

  @ExceptionHandler(BidOnOwnAuction.class)
  public ResponseEntity<Object> handleBidOnOwnAuction(BidOnOwnAuction ex) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);
    return new ResponseEntity<>(
        new Problem(BAD_REQUEST.value(), BAD_REQUEST.name(), ex.getMessage()), headers,
        BAD_REQUEST);
  }


  @ExceptionHandler(BidOthersAuction.class)
  public ResponseEntity<Object> handleBidOtherUsersAuction(BidOthersAuction ex) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);
    return new ResponseEntity<>(
        new Problem(BAD_REQUEST.value(), BAD_REQUEST.name(), ex.getMessage()), headers,
        BAD_REQUEST);
  }

  @ExceptionHandler(NotAuthorizedException.class)
  public ResponseEntity<Object> handleBidOtherUsersAuction(NotAuthorizedException ex) {
    var headers = new HttpHeaders();
    headers.setContentType(APPLICATION_PROBLEM_JSON);
    return new ResponseEntity<>(
        new Problem(UNAUTHORIZED.value(), UNAUTHORIZED.name(), ex.getMessage()), headers,
        UNAUTHORIZED);
  }

}
