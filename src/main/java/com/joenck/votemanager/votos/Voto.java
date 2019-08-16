package com.joenck.votemanager.votos;

import com.joenck.votemanager.pautas.Pauta;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Decisao decisao;

    @NotNull
    private String cpf;

    @JoinColumn(name="pauta_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Pauta pauta;

    public Voto() {}

    public Voto(Pauta pauta, String cpf, Decisao decisao) {
        this.pauta = pauta;
        this.cpf = cpf;
        this.decisao = decisao;
    }

    public Decisao getDecisao() {
        return decisao;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "id=" + id +
                ", cpf=" + cpf +
                ", decisao=" + decisao +
                ", pauta=" + pauta.getDescricao() +
                '}';
    }

    public enum Decisao {
        SIM("Sim"),
        NÃO("Não");

        private String valor;

        Decisao(String valor) {
            this.valor = valor;
        }

        public static Decisao get(String valor) {
            for(Decisao decisao : Decisao.values()) {
                if (decisao.valor.equalsIgnoreCase(valor)) {
                    return decisao;
                }
            }
            throw new IllegalArgumentException("Decisão inválida. Escolha entre Sim e Não");
        }
    }
}
