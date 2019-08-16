package com.joenck.votemanager.pautas;


import com.joenck.votemanager.votos.Voto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    private String descricao;

    private ZonedDateTime limiteVotacao;

    @OneToMany(mappedBy = "pauta",fetch = FetchType.EAGER)
    private List<Voto> votos;

    public Pauta(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ZonedDateTime getLimiteVotacao() {
        return limiteVotacao;
    }

    public void setLimiteVotacao(ZonedDateTime limiteVotacao) {
        this.limiteVotacao = limiteVotacao;
    }

    @Override
    public String toString() {
        return "Pauta{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", limiteVotacao=" + limiteVotacao +
                ", votos=" + votos +
                '}';
    }
}
