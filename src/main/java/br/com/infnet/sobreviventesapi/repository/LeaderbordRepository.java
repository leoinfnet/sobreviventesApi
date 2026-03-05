package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.domain.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderbordRepository extends JpaRepository<Leaderboard, Long> {
}
