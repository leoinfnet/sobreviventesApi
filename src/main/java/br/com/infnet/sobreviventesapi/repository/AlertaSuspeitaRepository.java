package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.domain.AlertaSuspeita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertaSuspeitaRepository extends JpaRepository<AlertaSuspeita, Long> {
    Optional<AlertaSuspeita> findByAlertaId(String alertaId);
}
