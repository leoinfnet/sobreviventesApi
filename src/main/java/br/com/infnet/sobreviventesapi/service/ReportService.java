package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.SobreviventeRawResponse;
import br.com.infnet.sobreviventesapi.api.dto.report.ContagemInfectadosReport;
import br.com.infnet.sobreviventesapi.api.dto.report.EstoquePorRecursoReport;
import br.com.infnet.sobreviventesapi.api.dto.report.InfectadoEmZonaSeguraReport;
import br.com.infnet.sobreviventesapi.api.dto.report.SobreviventesPorComunidadeReport;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import br.com.infnet.sobreviventesapi.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    @Transactional(readOnly = true)
    public ContagemInfectadosReport contagemInfectados() {
        return reportRepository.reportInfectados();
    }

    @Transactional(readOnly = true)
    public List<SobreviventesPorComunidadeReport> sobreviventesPorComunidade() {
        return reportRepository.sobreviventesPorComunidade();
    }


    @Transactional(readOnly = true)
    public List<InfectadoEmZonaSeguraReport> infectadosEmZonasSeguras() {
        return reportRepository.infectadosEmZonasSeguras();
    }

    @Transactional(readOnly = true)
    public Slice<SobreviventeRawResponse> sliceInfectados(Pageable pageable) {
        return reportRepository.sliceInfectados(pageable)
                .map(this::toRawResponse);
    }

    private SobreviventeRawResponse toRawResponse(Sobrevivente s) {
        return new SobreviventeRawResponse(
                s.getId(),
                s.getNome(),
                s.getLocalizacao(),
                s.isInfectado()
        );
    }

}
