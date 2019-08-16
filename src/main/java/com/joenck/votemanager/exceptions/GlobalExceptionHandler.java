package com.joenck.votemanager.exceptions;

import org.springframework.http.HttpStatus;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        String mensagem = e.getMessage().contains("must not be null") ?  "Descrição não pode ser nula" : "Descrição não pode ultrapassar de 255 caracteres";
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), mensagem);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String mensagem = "Formato de parametros não reconhecido";
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), mensagem);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({HttpClientErrorException.class})
    public ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) {
        String mensagem = "Associado não existe";
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(),mensagem);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoDataFoundException.class})
    public ErrorResponse handleNoDataFoundException(NoDataFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DateTimeParseException.class})
    public ErrorResponse handleDateTimeParseException(){
        String mensagem = "Formato de data inválido. Padrão Esperado: yyyy-MM-dd HH:mm:ss VV";
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), mensagem);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DuplicateVoteException.class,IllegalArgumentException.class,ClosedVoteException.class})
    public ErrorResponse handleDuplicateVoteException(RuntimeException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
