package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.domain.Leaderboard;
import br.com.infnet.sobreviventesapi.repository.LeaderbordRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {
    private final LeaderbordRepository repository;

    public LeaderboardService(LeaderbordRepository repository) {
        this.repository = repository;
    }
    @Cacheable(cacheNames = "leaderboardSEmanal", key = "'top10:latweek'")
    public List<Leaderboard> find(){
        System.out.println("Calculando LeaderBoard do banco");
        return repository.findAll();
    }
    @CacheEvict(cacheNames = "leaderboardSEmanal", key = "'top10:latweek'")
    public void evictCache(){

    }

}
