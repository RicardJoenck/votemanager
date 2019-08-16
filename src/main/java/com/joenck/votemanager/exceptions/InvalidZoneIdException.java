package com.joenck.votemanager.exceptions;

public class InvalidZoneIdException extends RuntimeException {
    public InvalidZoneIdException(String fusoHorario) {
        super(String.format("Fuso horario %s ainda não é suportado, somente America/Sao_Paulo",fusoHorario));
    }
}
