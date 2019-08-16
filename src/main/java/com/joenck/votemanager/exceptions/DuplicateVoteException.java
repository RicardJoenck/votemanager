package com.joenck.votemanager.exceptions;

public class DuplicateVoteException extends RuntimeException {
    public DuplicateVoteException(String cpf, Long pautaId) {
        super(String.format("Já existe um voto do cpf %s para pauta %d",cpf,pautaId));
    }
}
