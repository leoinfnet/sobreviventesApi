package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.report.LeaderboardRow;
import br.com.infnet.sobreviventesapi.repository.SurvivorActivityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final SurvivorActivityEventRepository repository;
    private final Clock clock;
    @Cacheable(cacheNames = "weeklyLeaderboard",key = "'top10:last7d'")
    public List<LeaderboardRow> top10LastWeek() {
        System.out.println("CALCULANDO LEADERBOARD NO BANCO...");
        return topLastWeek(10);
    }

    public List<LeaderboardRow> topLastWeek(int safeTop) {
        Instant since = Instant.now(clock).minus(7, ChronoUnit.DAYS);
        return repository.leaderboardSince(since, PageRequest.of(0, safeTop));


    }
    @CacheEvict(cacheNames = "weeklyLeaderboard", key = "'top10:last7d'")
    public void evictWeeklyLeaderboard() {
        // vazio mesmo: só serve pra disparar o evict via proxy
    }
}
