package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.ComunidadeResponse;
import br.com.infnet.sobreviventesapi.api.dto.CriarComunidadeRequest;
import br.com.infnet.sobreviventesapi.domain.Comunidade;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import br.com.infnet.sobreviventesapi.repository.ComunidadeRepository;
import br.com.infnet.sobreviventesapi.repository.SobreviventeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComunidadeService {
    private final ComunidadeRepository comunidadeRepository;
    private final SobreviventeRepository sobreviventeRepository;

    public ComunidadeResponse criar(CriarComunidadeRequest request) {
        Comunidade c = new Comunidade(request.nome(), request.zonaSegura());
        return toResponse(comunidadeRepository.save(c));
    }

    @Transactional(readOnly = true)
    public ComunidadeResponse buscarPorId(Long id) {
        Comunidade c = comunidadeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
        return toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<ComunidadeResponse> listarTodas() {
        return comunidadeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void adicionarMembro(Long comunidadeId, Long sobreviventeId) {
        Comunidade comunidade = comunidadeRepository.findById(comunidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));

        Sobrevivente sobrevivente = sobreviventeRepository.findById(sobreviventeId)
                .orElseThrow(() -> new IllegalArgumentException("Sobrevivente não encontrado"));

        // usa regra de domínio já implementada no sobrevivente
        sobrevivente.entrarNaComunidade(comunidade);
    }

    private ComunidadeResponse toResponse(Comunidade c) {
        return new ComunidadeResponse(c.getId(), c.getNome(), c.isZonaSegura());
    }
}
