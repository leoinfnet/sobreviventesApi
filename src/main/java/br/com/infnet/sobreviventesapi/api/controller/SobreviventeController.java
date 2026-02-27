package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.ContagemResponse;
import br.com.infnet.sobreviventesapi.api.dto.SobreviventeResponse;
import br.com.infnet.sobreviventesapi.api.dto.SobreviventeSimples;
import br.com.infnet.sobreviventesapi.service.SobreviventeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        //Dirty Checking
        //update sobrevivente set localizacao = 'sp' where id = 1
    }
    @GetMapping
    public ResponseEntity<List<SobreviventeSimples>> buscarTodos(@PageableDefault(size=10,sort = "id") Pageable pageable){
        Page<SobreviventeSimples> page = service.buscarTodos(pageable);

        return ResponseEntity.ok()
                .header("X-Total-Count",String.valueOf(page.getTotalElements()))
                .header("X-Total-Pages",String.valueOf(page.getTotalPages()))
                .header("X-Page-Number",String.valueOf(page.getNumber()))
                .header("X-Page-Size",String.valueOf(page.getSize()))
                .body(page.getContent());
    }
    @PatchMapping("/{id}/infectado")
    public ResponseEntity<Void> infectar(@PathVariable Long id){
        service.marcarComoInfectado(id);
        return ResponseEntity.noContent().build();

    }
    @GetMapping("/contagem")
    public ResponseEntity<ContagemResponse> obterContagem(){
        return ResponseEntity.ok(service.contagem());
    }

}
