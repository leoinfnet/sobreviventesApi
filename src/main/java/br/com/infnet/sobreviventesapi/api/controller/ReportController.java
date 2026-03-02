package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.SobreviventeRawResponse;
import br.com.infnet.sobreviventesapi.api.dto.report.ContagemInfectadosReport;
import br.com.infnet.sobreviventesapi.api.dto.report.EstoquePorRecursoReport;
import br.com.infnet.sobreviventesapi.api.dto.report.InfectadoEmZonaSeguraReport;
import br.com.infnet.sobreviventesapi.api.dto.report.SobreviventesPorComunidadeReport;
import br.com.infnet.sobreviventesapi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")@RequiredArgsConstructor
public class ReportController {
    private final ReportService service;


    @GetMapping("/infectados/contagem")
    public ResponseEntity<ContagemInfectadosReport> contagemInfectados() {
        return ResponseEntity.ok(service.contagemInfectados());
    }

    @GetMapping("/comunidades/sobreviventes")
    public ResponseEntity<List<SobreviventesPorComunidadeReport>> sobreviventesPorComunidade() {
        return ResponseEntity.ok(service.sobreviventesPorComunidade());
    }


    @GetMapping("/zonas-seguras/infectados")
    public ResponseEntity<List<InfectadoEmZonaSeguraReport>> infectadosEmZonasSeguras() {
        return ResponseEntity.ok(service.infectadosEmZonasSeguras());
    }

    // Extra para slide: Slice sem count(*) + headers úteis
    @GetMapping("/infectados")
    public ResponseEntity<List<SobreviventeRawResponse>> sliceInfectados(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Slice<SobreviventeRawResponse> slice = service.sliceInfectados(pageable);

        int nextPage = slice.hasNext() ? slice.getNumber() + 1 : slice.getNumber();

        return ResponseEntity.ok()
                .header("X-Has-Next", String.valueOf(slice.hasNext()))
                .header("X-Page-Number", String.valueOf(slice.getNumber()))
                .header("X-Page-Size", String.valueOf(slice.getSize()))
                .header("X-Next-Page", slice.hasNext() ? String.valueOf(nextPage) : "")
                .body(slice.getContent());
    }
}
