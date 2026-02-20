package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.domain.Comunidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComunidadeRepository extends JpaRepository<Comunidade, Long> {
}
