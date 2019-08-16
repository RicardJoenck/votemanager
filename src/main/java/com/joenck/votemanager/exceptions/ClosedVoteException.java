package com.joenck.votemanager.exceptions;

public class ClosedVoteException extends RuntimeException {
    public ClosedVoteException(Long id) {
        super(String.format("Votação para pauta %d não está aberta/disponível",id));
    }
}
