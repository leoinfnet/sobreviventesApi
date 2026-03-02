package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.api.dto.report.LeaderboardRow;
import br.com.infnet.sobreviventesapi.domain.SurvivorActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SurvivorActivityEventRepository extends
        JpaRepository<SurvivorActivityEvent, Long> {
    @Query("""
        select new br.com.infnet.sobreviventesapi.api.dto.report.LeaderboardRow(
            e.survivorId,
            sum(e.scoreValue)
        )
        from SurvivorActivityEvent e
        where e.occurredAt >= :since
        group by e.survivorId
        order by sum(e.scoreValue) desc
    """)
    List<LeaderboardRow> leaderboardSince(Instant since, org.springframework.data.domain.Pageable pageable);
}
