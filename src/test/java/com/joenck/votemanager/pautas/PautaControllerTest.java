package com.joenck.votemanager.pautas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PautaController.class)
public class PautaControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private PautaController pautaController;

    @Test
    public void quandoCadastrarPautaComDescricaoValida_EntaoRetornePauta() throws Exception {
        String descricao = "Teste";
        Pauta pauta = new Pauta(descricao);
        Map<String,String> json = new HashMap<>();
        json.put("descricao",descricao);

        String requestJson=jsonFormatter(json);
        Mockito.when(pautaController.cadastrarPauta(json)).thenReturn(pauta);

        mvc.perform(post("/api/1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(jsonPath("$.descricao", is(pauta.getDescricao())));
    }

    @Test
    public void quandoCadastrarPautaComDescricaoVazia_EntaoRetorneBadRequest() throws Exception {
        Map<String,String> json = new HashMap<>();
        json.put("descricao","");
        String requestJson = jsonFormatter(json);
        Mockito.when(pautaController.cadastrarPauta(json)).thenThrow(new IllegalArgumentException("Descrição não pode ser vazia"));

        mvc.perform(post("/api/1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void quandoAbrirSessaoComDataLimiteValida_EntaoRetornePauta() throws Exception {
        String descricao = "Teste";
        Pauta pauta = new Pauta(descricao);
        Map<String,String> json = new HashMap<>();
        json.put("limiteVotacao","2019-08-30 13:30:30 America/Sao_Paulo");
        String requestJson=jsonFormatter(json);
        pauta.setLimiteVotacao(ZonedDateTime.parse("2019-08-30 13:30:30 America/Sao_Paulo", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV")));
        Mockito.when(pautaController.abrirSessao(1L,json)).thenReturn(pauta);

        mvc.perform(put("/api/1/pautas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(jsonPath("$.limiteVotacao", is("2019-08-30T13:30:30-03:00")));
    }

    @Test
    public void quandoAbrirSessaoComPautaJaAberta_EntaoRetorneBadRequest() throws Exception {
        Map<String,String> json = new HashMap<>();
        json.put("limiteVotacao","2019-08-30 13:30:30");
        String requestJson=jsonFormatter(json);
        Mockito.when(pautaController.abrirSessao(1L,json)).thenThrow(new IllegalStateException(String.format("Pauta %s já tem uma data limite para votação",1L)));

        mvc.perform(put("/api/1/pautas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    private String jsonFormatter(Map<String,String> o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }
}
