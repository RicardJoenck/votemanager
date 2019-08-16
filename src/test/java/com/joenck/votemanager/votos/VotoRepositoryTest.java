package com.joenck.votemanager.votos;

import com.joenck.votemanager.exceptions.NoDataFoundException;
import com.joenck.votemanager.pautas.Pauta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VotoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VotoRepository votoRepository;

    @Test
    public void quandoFindById_entaoRetorneVoto() throws NoDataFoundException {
        Pauta pauta = new Pauta("Votação test");
        Voto voto = new Voto(pauta,86935275091L,Decisao.NÃO);
        entityManager.persist(voto);
        Voto resultado = votoRepository.findById(1L).orElseThrow(() -> new NoDataFoundException("NENHUMA_PAUTA_FOI_ENCONTRADA"));
        assertThat(resultado.getPauta().getDescricao()).isEqualTo("Votação test");
    }

    @Test
    public void quandoFindAll_entaoRetorneListaVotos() {
        Pauta pauta = new Pauta("Votação test");
        Voto voto = new Voto(pauta,86935275091L,Decisao.NÃO);
        Voto voto2 = new Voto(pauta,86935275092L,Decisao.NÃO);
        entityManager.persist(voto);
        entityManager.persist(voto2);
        List<Voto> votos = votoRepository.findAll();
        assertThat(votos).hasSize(2);
    }

    @Test
    public void quandoInserirPautaNula_entaoRetorneConstraintViolationException() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            Voto votoInvalida = new Voto(null,86935275092L,Decisao.NÃO);
            entityManager.persist(votoInvalida);
            entityManager.flush();
        }).withMessageContaining("'must not be null'");
    }

    @Test
    public void quandoInserirCpfNulo_entaoRetorneConstraintViolationException() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            Pauta pauta = new Pauta("Votação test");
            Voto votoInvalida = new Voto(pauta,null,Decisao.NÃO);
            entityManager.persist(votoInvalida);
            entityManager.flush();
        }).withMessageContaining("'must not be null'");
    }

    @Test
    public void quandoInserirDescricaoNula_entaoRetorneConstraintViolationException() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            Pauta pauta = new Pauta("Votação test");
            Voto votoInvalida = new Voto(pauta,86935275092L,null);
            entityManager.persist(votoInvalida);
            entityManager.flush();
        }).withMessageContaining("'must not be null'");
    }

}
