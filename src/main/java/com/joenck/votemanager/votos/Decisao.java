package com.joenck.votemanager.votos;

public enum Decisao {
    SIM("Sim"),
    NÃO("Não");

    private String valor;

    Decisao(String valor) {
        this.valor = valor;
    }

    public static Decisao get(String valor) {
        for(Decisao decisao : values()) {
            if (decisao.valor.equalsIgnoreCase(valor)) {
                return decisao;
            }
        }
        throw new IllegalArgumentException("DECISÃO_INVÁLIDA");
    }
}
