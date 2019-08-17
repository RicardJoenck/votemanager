package com.joenck.votemanager.votos;

import com.joenck.votemanager.exceptions.ClosedVoteException;
import com.joenck.votemanager.exceptions.DuplicateVoteException;
import com.joenck.votemanager.pautas.Pauta;
import com.joenck.votemanager.pautas.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VotoService {

    @Autowired
    VotoRepository votoRepository;

    @Autowired
    PautaService pautaService;

    public Voto registrarVoto(Long pautaId, String cpf, String decisao) {
        if(pautaService.votacaoAberta(pautaId)) {
            Pauta pauta = pautaService.getById(pautaId);
            if(!votoRepository.existsByCpfAndPauta(cpf,pauta)){
                Voto voto = new Voto(pauta, cpf, Voto.Decisao.get(decisao));
                return votoRepository.save(voto);
            } else {
                throw new DuplicateVoteException(cpf,pauta.getId());
            }
        } else {
            throw new ClosedVoteException(pautaId);
        }
    }

    public Map<String, Long> contagemVotos(Long pautaId) {
        Map<String, Long> resultado = votoRepository.findAllByPauta(pautaService.getById(pautaId))
            .stream()
            .map(Voto::getDecisao)
            .collect(Collectors.groupingBy(Voto.Decisao::name,Collectors.counting()));
        return resultado;

}

}
