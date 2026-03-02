package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.ComunidadeResponse;
import br.com.infnet.sobreviventesapi.api.dto.CriarComunidadeRequest;
import br.com.infnet.sobreviventesapi.service.ComunidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comunidades")
@RequiredArgsConstructor
public class ComunidadeController {
    private final ComunidadeService service;
    @PostMapping
    public ResponseEntity<ComunidadeResponse> criar(@Valid @RequestBody CriarComunidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunidadeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ComunidadeResponse>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping("/{id}/membros/{sobreviventeId}")
    public ResponseEntity<Void> adicionarMembro(@PathVariable Long id, @PathVariable Long sobreviventeId) {
        service.adicionarMembro(id, sobreviventeId);
        return ResponseEntity.noContent().build();
    }
}
