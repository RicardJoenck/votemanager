package com.joenck.votemanager.pautas;

import com.joenck.votemanager.exceptions.NoDataFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PautaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PautaRepository pautaRepository;

    @Before
    public void setUp(){
        Pauta pautaValida = new Pauta("Votação teste 1");
        Pauta pautaValida2 = new Pauta("Votação teste 2");
        entityManager.persist(pautaValida);
        entityManager.persist(pautaValida2);
    }

    @Test
    public void quandoFindById_entaoRetornePauta() throws NoDataFoundException {
        Pauta pauta = pautaRepository.findById(1L).orElseThrow(() -> new NoDataFoundException(1L,Pauta.class.getSimpleName()));
        System.out.println(pauta.toString());
        assertThat(pauta.getDescricao()).isEqualTo("Votação teste 1");
    }

    @Test
    public void quandoFindAll_entaoRetorneListaPautas() {
        List<Pauta> pautas = pautaRepository.findAll();
        assertThat(pautas).hasSize(2);
    }

    @Test
    public void quandoFindByIdInexistente_entaoRetorneNoDataFoundExceptionException() {
        Long id = 999L;
        String className = Pauta.class.getSimpleName();
        assertThatExceptionOfType(NoDataFoundException.class).isThrownBy(() -> {
            Pauta pauta = pautaRepository.findById(999L).orElseThrow(() -> new NoDataFoundException(id,className));
        }).withMessageContaining(String.format("Nenhum Dado achado com id %d para %s",id,className));
    }

    @Test
    public void quandoInserirDescricaoLonga_entaoRetorneConstraintViolationException() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            Pauta pautaInvalida = new Pauta("DescricaoExtremamenteLongaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            entityManager.persist(pautaInvalida);
            entityManager.flush();
        }).withMessageContaining("'size must be between 0 and 255'");
    }

    @Test
    public void quandoInserirDescricaoNula_entaoRetorneConstraintViolationException() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> {
            Pauta pautaInvalida = new Pauta(null);
            entityManager.persist(pautaInvalida);
            entityManager.flush();
        }).withMessageContaining("'must not be null'");
    }
}
