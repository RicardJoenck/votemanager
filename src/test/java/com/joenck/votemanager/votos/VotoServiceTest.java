package com.joenck.votemanager.votos;

import com.joenck.votemanager.exceptions.ClosedVoteException;
import com.joenck.votemanager.exceptions.DuplicateVoteException;
import com.joenck.votemanager.pautas.Pauta;
import com.joenck.votemanager.pautas.PautaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private VotoService votoService;

    @Test
    public void quandoContagemVotosComIdValido_EntaoRetorneResultado()
    {
        Pauta pauta = new Pauta("Pauta teste");
        Voto voto1 = new Voto(pauta, "1234565", Voto.Decisao.SIM);
        Voto voto2 = new Voto(pauta, "1234567", Voto.Decisao.NÃO);
        Voto voto3 = new Voto(pauta, "1234568", Voto.Decisao.SIM);
        List<Voto> votos = Arrays.asList(voto1, voto2, voto3);
        Mockito.when(pautaService.getById(1L)).thenReturn(pauta);
        Mockito.when(votoRepository.findAllByPauta(pauta)).thenReturn(votos);

        Map<String, Long> resultado = votoService.contagemVotos(1L);
        assertThat(resultado).containsKeys("SIM", "NÃO").containsValues(1L,2L);
    }

    @Test
    public void quandoRegistrarVotoComPautaEIdECpfEDecisaoValidos_EntaoRetorneVoto()
    {
        Pauta pauta = new Pauta("Pauta teste");
        String cpf = "19839091069";
        Voto voto = new Voto(pauta, cpf, Voto.Decisao.SIM);

        Mockito.when(pautaService.votacaoAberta(1L)).thenReturn(true);
        Mockito.when(pautaService.getById(1L)).thenReturn(pauta);
        Mockito.when(votoRepository.existsByCpfAndPauta(cpf,pauta)).thenReturn(false);
        Mockito.when(votoRepository.save(Mockito.any(Voto.class))).thenReturn(voto);

        Voto votoResultado = votoService.registrarVoto(1L, "19839091069", "Sim");
        assertThat(votoResultado.getCpf()).isEqualTo(voto.getCpf());
    }

    @Test
    public void quandoRegistrarVotoComPautaEmSessaoEncerrada_EntaoRetorneClosedVoteException()
    {
        assertThatExceptionOfType(ClosedVoteException.class).isThrownBy(() -> {
            Mockito.when(pautaService.votacaoAberta(1L)).thenReturn(false);
            votoService.registrarVoto(1L, "19839091069", "Sim");
        });

    }

    @Test
    public void quandoRegistrarVotoComVotoDuplicado_EntaoRetorneDuplicateVoteException()
    {
        assertThatExceptionOfType(DuplicateVoteException.class).isThrownBy(() -> {
            Pauta pauta = new Pauta("Pauta teste");
            Mockito.when(pautaService.votacaoAberta(1L)).thenReturn(true);
            Mockito.when(pautaService.getById(1L)).thenReturn(pauta);
            Mockito.when(votoRepository.existsByCpfAndPauta("19839091069",pauta)).thenReturn(true);
            votoService.registrarVoto(1L, "19839091069", "Sim");
        });
    }
}
