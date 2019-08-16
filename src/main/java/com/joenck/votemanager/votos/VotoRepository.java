package com.joenck.votemanager.votos;

import com.joenck.votemanager.pautas.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto,Long> {
    List<Voto> findAllByPauta(Pauta pauta);
    boolean existsByCpfAndPauta(String cpf, Pauta pauta);
}
