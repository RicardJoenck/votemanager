package com.joenck.votemanager.pautas;

import com.joenck.votemanager.exceptions.InvalidZoneIdException;
import com.joenck.votemanager.exceptions.NoDataFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    public void quandoAbrirSessaoComIdEDataValida_EntaoRetornePautaAtualizada() {
        ZonedDateTime data = ZonedDateTime.parse("2019-08-12 12:13:40 America/Sao_Paulo", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV"));
        Pauta pauta = new Pauta("Teste Válido");
        Optional<Pauta> optionalPauta = Optional.of(pauta);
        Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);
        Mockito.when(pautaRepository.save(pauta)).thenReturn(pauta);


        Pauta pautaResultado = pautaService.abrirSessao(1L, "2019-08-12 12:13:40 America/Sao_Paulo");

        assertThat(pautaResultado.getLimiteVotacao())
                .isEqualTo(data);
    }

    @Test
    public void quandoAbrirSessaoComIdESemData_EntaoRetornePautaAtualizada() {
        ZonedDateTime data = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(1).withSecond(0).withNano(0);
        Pauta pauta = new Pauta("Teste Válido");
        Optional<Pauta> optionalPauta = Optional.of(pauta);
        Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);
        Mockito.when(pautaRepository.save(pauta)).thenReturn(pauta);


        Pauta pautaResultado = pautaService.abrirSessao(1L, null);

        assertThat(pautaResultado.getLimiteVotacao().withSecond(0).withNano(0))
                .isEqualTo(data);
    }

    @Test
    public void quandoAbrirSessaoComIdInvalido_EntaoRetorneNoDataFoundException() {
        assertThatExceptionOfType(NoDataFoundException.class).isThrownBy(() -> {
            pautaService.abrirSessao(1L, null);
        }).withMessageContaining("Nenhum dado encontrado");
    }

    @Test
    public void quandoAbrirSessaoComZoneIdInvalido_EntaoRetorneInvalidZoneIdException() {
        assertThatExceptionOfType(InvalidZoneIdException.class).isThrownBy(() -> {
            Pauta pauta = new Pauta("Teste inválido");
            Optional<Pauta> optionalPauta = Optional.of(pauta);
            Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);
            pautaService.abrirSessao(1L, "2019-08-12 12:13:40 Australia/Sydney");
        }).withMessageContaining("ainda não é suportado");
    }

    @Test
    public void quandoAbrirSessaoEmPautaComSessaoAberta_EntaoRetorneIllegalStateException() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
            ZonedDateTime data = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(1).withNano(0);
            Pauta pauta = new Pauta("Teste inválido");
            pauta.setLimiteVotacao(data);
            Optional<Pauta> optionalPauta = Optional.of(pauta);
            Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);
            pautaService.abrirSessao(1L, null);
        }).withMessageContaining("data limite para votação");
    }

    @Test
    public void quandoAbrirSessaoComDataInvalida_EntaoRetorneDateTimeParseException() {
        assertThatExceptionOfType(DateTimeParseException.class).isThrownBy(() -> {
            Pauta pauta = new Pauta("Teste inválido");
            Optional<Pauta> optionalPauta = Optional.of(pauta);
            Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);
            pautaService.abrirSessao(1L, "Esse não é um formato correto");
        }).withMessageContaining("could not be parsed");
    }

    @Test
    public void quandoVotacaoAbertaComIdValidoESessaoAberta_EntaoRetorneTrue() {
        ZonedDateTime data = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusMinutes(30);
        Pauta pauta = new Pauta("Teste Válido");
        pauta.setLimiteVotacao(data);
        Optional<Pauta> optionalPauta = Optional.of(pauta);
        Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);

        Boolean resultado = pautaService.votacaoAberta(1L);

        assertThat(resultado).isEqualTo(true);
    }

    @Test
    public void quandoVotacaoAbertaComIdValidoESessaoEncerrada_EntaoRetorneFlase() {
        ZonedDateTime data = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).minusMinutes(30);
        Pauta pauta = new Pauta("Teste Válido");
        pauta.setLimiteVotacao(data);
        Optional<Pauta> optionalPauta = Optional.of(pauta);
        Mockito.when(pautaRepository.findById(1L)).thenReturn(optionalPauta);


        Boolean resultado = pautaService.votacaoAberta(1L);

        assertThat(resultado).isEqualTo(false);
    }

}
