package com.joenck.votemanager.exceptions;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException(String cpf) {
        super(String.format("Associado %s não está apto para votar",cpf));
    }
}
