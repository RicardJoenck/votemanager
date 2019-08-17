package com.joenck.votemanager.exceptions;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(Long id, String className) {
        super(String.format("Nenhum dado encontrado com id %d para %s",id,className));
    }
}
