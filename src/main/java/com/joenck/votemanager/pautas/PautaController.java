package com.joenck.votemanager.pautas;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/api/1")
public class PautaController {

    @Autowired
    PautaService pautaService;

    @PostMapping("/pautas")
    public Pauta cadastrarPauta(@RequestBody Map<String,String> body) {
        return pautaService.cadastrarPauta(body.get("descricao"));
    }

    @PutMapping("/pautas/{id}")
    public Pauta abrirSessao(@PathVariable("id") Long id, @RequestBody Map<String,String> body) {
        return pautaService.abrirSessao(id, body.get("limiteVotacao"));
    }

}
