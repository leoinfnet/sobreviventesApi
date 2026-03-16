package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //Opcional!
public interface SobreviventeRepository extends JpaRepository<Sobrevivente, Long> {
    @Query("""
            SELECT s from Sobrevivente s
            LEFT JOIN fetch s.recursos
           """)
    List<Sobrevivente> buscarTodosComRecursos();

//    @EntityGraph(attributePaths = {"recursos"})
//    List<Sobrevivente> findAll();

    @EntityGraph(attributePaths = {"recursos"})
    @Query("select s from Sobrevivente s")
    List<Sobrevivente> buscarComRecursos();

    //Todo avaliar criacao e indice quando migrar para postgres
    //Todo avaliar criacao de cache.
    long countAllByInfectado(boolean infectado);


}
