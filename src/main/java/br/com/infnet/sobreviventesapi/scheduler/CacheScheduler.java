package br.com.infnet.sobreviventesapi.scheduler;

import br.com.infnet.sobreviventesapi.service.LeaderboardService;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {
    private final CacheManager cache;
    private final LeaderboardService service;

    public CacheScheduler(CacheManager cache, LeaderboardService service) {
        this.cache = cache;
        this.service = service;
    }
    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 0 ? * DOM")
    public void clearCache(){
        System.out.println("Limpando o cache");
        service.evictCache();
        System.out.println("Aquecendo o cache");
        service.find();

    }

}
