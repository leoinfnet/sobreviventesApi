package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.domain.AlertaSuspeita;
import br.com.infnet.sobreviventesapi.repository.AlertaSuspeitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaSuspeitaService {
    private final AlertaSuspeitaRepository repository;

    public AlertaSuspeita criar(AlertaSuspeita alerta) {

        alerta.setCriadoEm(LocalDateTime.now());
        alerta.setAtualizadoEm(LocalDateTime.now());

        return repository.save(alerta);
    }

    public List<AlertaSuspeita> listarTodos() {
        return repository.findAll();
    }

    public AlertaSuspeita buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta não encontrado"));
    }

    public AlertaSuspeita atualizar(Long id, AlertaSuspeita alertaAtualizado) {

        AlertaSuspeita alerta = buscarPorId(id);

        alerta.setTitulo(alertaAtualizado.getTitulo());
        alerta.setDescricao(alertaAtualizado.getDescricao());
        alerta.setTipo(alertaAtualizado.getTipo());
        alerta.setGravidade(alertaAtualizado.getGravidade());
        alerta.setStatus(alertaAtualizado.getStatus());
        alerta.setRegiao(alertaAtualizado.getRegiao());
        alerta.setLocalTexto(alertaAtualizado.getLocalTexto());
        alerta.setLatitude(alertaAtualizado.getLatitude());
        alerta.setLongitude(alertaAtualizado.getLongitude());
        alerta.setFonte(alertaAtualizado.getFonte());
        alerta.setConfiabilidade(alertaAtualizado.getConfiabilidade());
        alerta.setDataHora(alertaAtualizado.getDataHora());
        alerta.setPalavrasChave(alertaAtualizado.getPalavrasChave());
        alerta.setSobreviventesRelacionados(alertaAtualizado.getSobreviventesRelacionados());
        alerta.setComunidadesRelacionadas(alertaAtualizado.getComunidadesRelacionadas());
        alerta.setResponsavelRegistro(alertaAtualizado.getResponsavelRegistro());
        alerta.setObservacoesInternas(alertaAtualizado.getObservacoesInternas());

        alerta.setAtualizadoEm(LocalDateTime.now());

        return repository.save(alerta);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
