package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.api.dto.report.ContagemInfectadosReport;
import br.com.infnet.sobreviventesapi.api.dto.report.InfectadoEmZonaSeguraReport;
import br.com.infnet.sobreviventesapi.api.dto.report.SobreviventesPorComunidadeReport;
import br.com.infnet.sobreviventesapi.api.dto.report.TopSobreviventeRecursosReport;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //Opcional!
public interface SobreviventeRepository extends JpaRepository<Sobrevivente, Long> {
    @Query("""
        select distinct s from Sobrevivente s 
        left join fetch s.comunidades
        left join fetch s.recursos
        """)
    List<Sobrevivente> buscarTodos();
    @EntityGraph(attributePaths = {"comunidades","recursos"})
    @Query("select s from Sobrevivente s")
    List<Sobrevivente> buscarTodos2();

    long countAllByInfectado(boolean infectado);

    //List<Sobrevivente> findAllByInfectado(boolean infectado, Pageable pageable);
    Slice<Sobrevivente> findAllByInfectado(boolean infectado, Pageable pageable);



}
