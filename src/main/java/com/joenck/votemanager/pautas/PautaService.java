package com.joenck.votemanager.pautas;

import com.joenck.votemanager.exceptions.InvalidZoneIdException;
import com.joenck.votemanager.exceptions.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PautaService {

    @Autowired
    PautaRepository pautaRepository;

    private ZoneId fusoHorario = ZoneId.of("America/Sao_Paulo");

    public Pauta cadastrarPauta(String descricao) {
        return pautaRepository.save(new Pauta(descricao));
    }

    public Pauta abrirSessao(Long id, String limiteVotacao) {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new NoDataFoundException(id, Pauta.class.getSimpleName()));
        ZonedDateTime limiteVotacaoFormatado;

        if (pauta.getLimiteVotacao() == null) {
            //Criando data com default de 1 minuto caso nenhum dado for passado
            if (limiteVotacao == null) {
                limiteVotacaoFormatado = ZonedDateTime.now(fusoHorario).plusMinutes(1);
            } else {
                limiteVotacaoFormatado = ZonedDateTime.parse(limiteVotacao, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV"));
                if (!limiteVotacaoFormatado.getZone().equals(fusoHorario)) {
                    throw new InvalidZoneIdException(limiteVotacaoFormatado.getZone().getId());
                }
            }
        } else {
            throw new IllegalStateException(String.format("Pauta %s já tem uma data limite para votação",pauta.getId()));
        }

        pauta.setLimiteVotacao(limiteVotacaoFormatado);
        return pautaRepository.save(pauta);
    }

    public Pauta getById(Long id) {
        return pautaRepository.findById(id).orElseThrow(() -> new NoDataFoundException(id,Pauta.class.getSimpleName()));
    }

    public Boolean votacaoAberta(Long id) {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new NoDataFoundException(id,Pauta.class.getSimpleName()));
        return ZonedDateTime.now(fusoHorario).isBefore(pauta.getLimiteVotacao());
    }
}
