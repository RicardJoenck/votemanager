package com.joenck.votemanager.votos;

import com.joenck.votemanager.pautas.Pauta;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Voto {

    @EmbeddedId
    private VotoId id;

    @Enumerated(EnumType.STRING)
    @Size(max = 3)
    private Decisao decisao;

    @MapsId("votoId")
    @JoinColumn(name="PautaId", referencedColumnName="PautaId")
    @ManyToOne(cascade = CascadeType.ALL)
    private Pauta pauta;

    public Voto(VotoId id, Decisao decisao) {
        this.id = id;
        this.decisao = decisao;
    }
}

@Embeddable
class VotoId implements Serializable {
    @Column(name = "PautaId")
    private Long pautaId;

    @Column(name = "CPF")
    private Long cpf;

    public VotoId(Long pautaId, Long cpf) {
        this.pautaId = pautaId;
        this.cpf = cpf;
    }

    public Long getPautaId() {
        return pautaId;
    }

    public Long getCPF() {
        return cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VotoId)) return false;
        VotoId that = (VotoId) o;
        return Objects.equals(this.getCPF(), that.getCPF()) &&
                Objects.equals(this.getPautaId(), that.getPautaId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPautaId(), getCPF());
    }
}
