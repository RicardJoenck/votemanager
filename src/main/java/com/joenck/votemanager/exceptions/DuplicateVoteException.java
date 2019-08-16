package com.joenck.votemanager.exceptions;

public class DuplicateVoteException extends RuntimeException {
    public DuplicateVoteException(Long cpf, Long pautaId) {
        super(String.format("Já existe um voto do cpf %d para pauta %d",cpf,pautaId));
    }
}
