package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.*;
import br.com.infnet.sobreviventesapi.api.exception.EntidadeNaoLocalizadaException;
import br.com.infnet.sobreviventesapi.domain.Comunidade;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import br.com.infnet.sobreviventesapi.repository.ComunidadeRepository;
import br.com.infnet.sobreviventesapi.repository.SobreviventeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SobreviventeService {
    private final SobreviventeRepository sobreviventeRepository;
    private final ComunidadeRepository comunidadeRepository;
    public SobreviventeResponse registrar(RegistrarSobreviventeResquest request){
        Sobrevivente s = new Sobrevivente(request.nome(), request.localizacao());
        return toResponse(sobreviventeRepository.save(s));
    }
    public Page<SobreviventeRawResponse> listarPaginado(Pageable pageable) {
        return sobreviventeRepository.findAll(pageable)
                .map(this::toRawResponse);
    }

    public List<SobreviventeResponse> buscarTodos(){
        return sobreviventeRepository.buscarTodos2().stream().map(this::toResponse).toList();
        //        return sobreviventeRepository.findAll()
//                .stream()
//                .map(this::toRawResponse)
//                .toList();
    }
    public List<SobreviventeResponse> listarNaoInfectados(){
        return
                sobreviventeRepository.findAll().stream()
                .filter(s -> !s.isInfectado())
                .map(this::toResponse)
                .toList();
    }
    public List<SobreviventeRawResponse> listarNaoInfectados2(Pageable pageable){
        return
               sobreviventeRepository
                       .findAllByInfectado(false,pageable).stream()
                       .map(this::toRawResponse)
                       .toList();
    }
    public Slice<SobreviventeRawResponse> listarNaoInfectados3(Pageable pageable){
        return
                sobreviventeRepository
                        .findAllByInfectado(false,pageable)
                        .map(this::toRawResponse);

    }

    @Transactional
    public void marcarComoInfectado(Long id){
        Sobrevivente s = buscar(id);   //Managed
        s.marcarComoInfectado();
        //Dirty Checking
    }
    public ContagemResponse contagem(){
        var all = sobreviventeRepository.findAll();
        long infectados = all.stream().filter(Sobrevivente::isInfectado).count();
        return new ContagemResponse(infectados,all.size() - infectados);
    }



    public SobreviventeResponse adicionarRecurso(Long sobreviventeId,
                                                 AdicionarRecursoRequest request){
        Sobrevivente sobrevivente = buscar(sobreviventeId);
        sobrevivente.adicionarRecurso(request.nome(),request.quantidade());
        return toResponse(sobrevivente);
        //Dirty Checking

    }
    @Transactional
    public void associarComunidade(Long id, Long comunidadeId) {
        Sobrevivente s = buscar(id);
        Comunidade c = comunidadeRepository.findById(comunidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
        s.entrarNaComunidade(c);
    }

    @Transactional
    public void removerRecurso(Long id, Long recursoId) {
        buscar(id).removerRecurso(recursoId);
    }
    private Sobrevivente buscar(Long id) {
        return sobreviventeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Sobrevivente Não encontrado"));
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
    private SobreviventeRawResponse toRawResponse(Sobrevivente sobrevivente) {
        return new SobreviventeRawResponse(sobrevivente.getId(), sobrevivente.getNome(),sobrevivente.getLocalizacao(), sobrevivente.isInfectado());
    }
    @Transactional(readOnly = true)
    public SobreviventeResponse buscarPorId(Long id) {
        Sobrevivente sobrevivente = sobreviventeRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoLocalizadaException("Sobrevivente Não encontrado"));
        return toResponse(sobrevivente);

    }
}
