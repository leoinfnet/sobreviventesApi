package br.com.infnet.sobreviventesapi.scheduler;

import br.com.infnet.sobreviventesapi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaderboardCacheScheduler {
    private final CacheManager cacheManager;
    private final LeaderboardService service;
    // Segunda-feira 00:00
    //@Scheduled(cron = "0 0 0 ? * MON")
    @Scheduled(fixedRate = 10000)
    public void clearWeeklyLeaderboardCache() {
        log.info("Limpando cache do leaderboard...");
      //  service.evictWeeklyLeaderboard();

        log.info("Aquecendo cache do leaderboard...");
      //  service.top10LastWeek();

        log.info("Cache do leaderboard aquecido com sucesso.");
    }
}
