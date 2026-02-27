package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.*;
import br.com.infnet.sobreviventesapi.api.exception.EntidadeNaoLocalizadaException;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import br.com.infnet.sobreviventesapi.repository.SobreviventeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SobreviventeService {
    private final SobreviventeRepository sobreviventeRepository;
    public SobreviventeResponse registrar(RegistrarSobreviventeResquest request){
        Sobrevivente s = new Sobrevivente(request.nome(), request.localizacao());
        return toResponse(sobreviventeRepository.save(s));
    }
    public List<SobreviventeResponse> listarNaoInfectados(){
        return
                sobreviventeRepository.findAll().stream()
                .filter(s -> !s.isInfectado())
                .map(this::toResponse)
                .toList();
    }
    @Transactional
    public void marcarComoInfectado(Long id){
        Sobrevivente s = buscar(id);   //Managed
        s.marcarComoInfectado();
        //Dirty Checking
    }
    public ContagemResponse contagem(){
        long total = sobreviventeRepository.count();
        long infectados = sobreviventeRepository
                .countAllByInfectado(true);
        return new ContagemResponse(infectados,total - infectados);
    }

    public SobreviventeResponse adicionarRecurso(Long sobreviventeId,
                                                 AdicionarRecursoRequest request){
        Sobrevivente sobrevivente = buscar(sobreviventeId);
        sobrevivente.adicionarRecurso(request.nome(),request.quantidade());
        return toResponse(sobrevivente);
        //Dirty Checking

    }
    private Sobrevivente buscar(Long id) {
        return sobreviventeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Sobrevivente Não encontrado"));
    }


    @Transactional(readOnly = true)
    public SobreviventeResponse buscarPorId(Long id) {
        Sobrevivente sobrevivente = sobreviventeRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoLocalizadaException("Sobrevivente Não encontrado"));
        return toResponse(sobrevivente);

    }

    public Page<SobreviventeSimples> buscarTodos(Pageable pageable){
        return sobreviventeRepository.findAll(pageable)
                .map(this::toResponseSimple);

    }
    private  SobreviventeSimples toResponseSimple(Sobrevivente saved) {
        return new SobreviventeSimples(
                saved.getId(), saved.getNome(), saved.getLocalizacao(),
                saved.isInfectado());

    }

        private  SobreviventeResponse toResponse(Sobrevivente saved) {
        return new SobreviventeResponse(
                saved.getId(), saved.getNome(), saved.getLocalizacao(),
                saved.isInfectado(),
                saved.getRecursos().stream()
                        .map(r -> new RecursoResponse(r.getId(),r.getNome(), r.getQuantidade()))
                        .toList(),
                saved.getComunidades().stream()
                        .map(c -> new ComunidadeResponse(c.getId(),c.getNome(),c.isZonaSegura()))
                        .collect(Collectors.toSet())
        );
    }

}
