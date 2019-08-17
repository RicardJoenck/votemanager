package com.joenck.votemanager.votos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.joenck.votemanager.exceptions.InvalidCpfException;
import com.joenck.votemanager.exceptions.NoDataFoundException;
import com.joenck.votemanager.pautas.Pauta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(VotoController.class)
public class VotoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VotoController votoController;

    @Test
    public void quandoRegistrarVotoComCpfEDecisaoValidos_EntaoRetorneVoto() throws Exception {
        String cpf = "19839091069";
        String descricao = "teste";
        Pauta pauta = new Pauta(descricao);
        Voto voto = new Voto(pauta,cpf, Voto.Decisao.SIM);
        Map<String,String> json = new HashMap<>();
        json.put("cpf",cpf);
        json.put("decisao","Sim");

        String requestJson=jsonFormatter(json);
        Mockito.when(votoController.registrarVoto(1L,json)).thenReturn(voto);

        mvc.perform(post("/api/1/pautas/1/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(jsonPath("$.cpf", is(voto.getCpf())));
    }

    @Test
    public void quandoRegistrarVotoComCpfInvalido_EntaoRetorneBadRequest() throws Exception {
        String cpf = "12345";
        String descricao = "teste";
        Pauta pauta = new Pauta(descricao);
        Voto voto = new Voto(pauta,cpf, Voto.Decisao.SIM);
        Map<String,String> json = new HashMap<>();
        json.put("cpf",cpf);
        json.put("decisao","Sim");

        String requestJson=jsonFormatter(json);
        Mockito.when(votoController.registrarVoto(1L,json)).thenThrow(new InvalidCpfException(cpf));

        mvc.perform(post("/api/1/pautas/1/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void quandoAbrirSessaoComDataLimiteValida_EntaoRetornePauta() throws Exception {
        Map<String,Long> resultado = new HashMap<>();
        resultado.put("SIM",1L);
        resultado.put("NÃO",2L);
        Mockito.when(votoController.contagemVotos(1L)).thenReturn(resultado);

        mvc.perform(get("/api/1/pautas/1/votos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.SIM", is(1)));
    }

    @Test
    public void quandoAbrirSessaoComPautaJaAberta_EntaoRetorneBadRequest() throws Exception {
        Map<String,Long> resultado = new HashMap<>();
        resultado.put("SIM",1L);
        resultado.put("NÃO",2L);
        Mockito.when(votoController.contagemVotos(1L)).thenThrow(new NoDataFoundException(1L,"Pauta"));

        mvc.perform(get("/api/1/pautas/1/votos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String jsonFormatter(Map<String,String> o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}
