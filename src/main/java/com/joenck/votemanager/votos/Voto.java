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
    private Long cpf;

    @JoinColumn(name="pauta_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Pauta pauta;

    public Voto(Pauta pauta, Long cpf, Decisao decisao) {
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

    public Long getCpf() {
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
}
