package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Opcional!
public interface SobreviventeRepository extends JpaRepository<Sobrevivente, Long> {
}
