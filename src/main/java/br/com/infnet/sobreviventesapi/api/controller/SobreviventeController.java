package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.AdicionarRecursoRequest;
import br.com.infnet.sobreviventesapi.api.dto.ContagemResponse;
import br.com.infnet.sobreviventesapi.api.dto.SobreviventeRawResponse;
import br.com.infnet.sobreviventesapi.api.dto.SobreviventeResponse;
import br.com.infnet.sobreviventesapi.service.SobreviventeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sobreviventes")
@RequiredArgsConstructor
public class SobreviventeController {
    private final SobreviventeService service;
    @GetMapping("/{id}")
    public ResponseEntity<SobreviventeResponse> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }
//    @GetMapping
//    public ResponseEntity<List<SobreviventeResponse>> buscarTodos(){
//        return ResponseEntity.ok(service.buscarTodos());
//    }
    @GetMapping
    public ResponseEntity<List<SobreviventeRawResponse>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<SobreviventeRawResponse> page = service.listarPaginado(pageable);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(page.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(page.getTotalPages()))
                .header("X-Page-Number", String.valueOf(page.getNumber()))
                .header("X-Page-Size", String.valueOf(page.getSize()))
                .body(page.getContent());
    }

    @GetMapping("/nao-infectados-sliced")
    public ResponseEntity<List<SobreviventeRawResponse>> listarNaoInfectados(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Slice<SobreviventeRawResponse> slice = service.listarNaoInfectados3(pageable);

        int nextPage = slice.hasNext() ? slice.getNumber() + 1 : slice.getNumber();

        return ResponseEntity.ok()
                .header("X-Has-Next", String.valueOf(slice.hasNext()))
                .header("X-Page-Number", String.valueOf(slice.getNumber()))
                .header("X-Page-Size", String.valueOf(slice.getSize()))
                .header("X-Next-Page", slice.hasNext() ? String.valueOf(nextPage) : "")
                .body(slice.getContent());
    }

    @PatchMapping("/{id}/infectado")
    public ResponseEntity<SobreviventeResponse> infectar(@PathVariable Long id){
        service.marcarComoInfectado(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/nao-infectados")
    public ResponseEntity<List<SobreviventeResponse>> listarNaoInfectados() {
        return ResponseEntity.ok(service.listarNaoInfectados());
    }

    @GetMapping("/contagem")
    public ResponseEntity<ContagemResponse> contagem() {
        return ResponseEntity.ok(service.contagem());
    }

    @PostMapping("/{id}/recursos")
    public ResponseEntity<SobreviventeResponse> adicionarRecurso(
            @PathVariable Long id,
            @Valid @RequestBody AdicionarRecursoRequest request) {
        return ResponseEntity.ok(service.adicionarRecurso(id, request));
    }

    @DeleteMapping("/{id}/recursos/{recursoId}")
    public ResponseEntity<Void> removerRecurso(
            @PathVariable Long id,
            @PathVariable Long recursoId) {
        service.removerRecurso(id, recursoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comunidades/{comunidadeId}")
    public ResponseEntity<Void> associar(
            @PathVariable Long id,
            @PathVariable Long comunidadeId) {
        service.associarComunidade(id, comunidadeId);
        return ResponseEntity.noContent().build();
    }

}
