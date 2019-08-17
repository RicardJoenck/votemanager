package com.joenck.votemanager.votos;

import com.joenck.votemanager.exceptions.InvalidCpfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.extern.log4j.Log4j2;
import java.util.Map;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping("/api/1")
public class VotoController {

    @Autowired
    VotoService votoService;

    @PostMapping("/pautas/{id}/votos")
    public Voto registrarVoto(@PathVariable("id") Long id, @RequestBody Map<String,String> body) {
        ResponseEntity<String> response = new RestTemplate().getForEntity(String.format("https://user-info.herokuapp.com/users/%s",body.get("cpf")), String.class);
        if(response.getBody().contains("UNABLE_TO_VOTE")) {
            throw new InvalidCpfException(body.get("cpf"));
        }
        return votoService.registrarVoto(id, body.get("cpf"), body.get("decisao"));
    }

    @GetMapping("/pautas/{id}/votos")
    public Map<String,Long> contagemVotos(@PathVariable("id") Long id) {
        return votoService.contagemVotos(id);
    }
}
