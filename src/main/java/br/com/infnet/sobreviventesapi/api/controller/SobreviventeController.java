package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.SobreviventeResponse;
import br.com.infnet.sobreviventesapi.service.SobreviventeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sobreviventes")
@RequiredArgsConstructor
public class SobreviventeController {
    private final SobreviventeService service;
    @GetMapping("/{id}")
    public ResponseEntity<SobreviventeResponse> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }
    @PatchMapping("/{id}/infectado")
    public ResponseEntity<SobreviventeResponse> infectar(@PathVariable Long id){
        service.marcarComoInfectado(id);
        return ResponseEntity.noContent().build();
    }

}
