package br.com.infnet.sobreviventesapi;

import br.com.infnet.sobreviventesapi.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SobreviventesApiApplicationTests {
    @Autowired
    LeaderboardService service;
    @Test
    void contextLoads() {
        System.out.println(service.find());
        System.out.println(service.find());
        service.evictCache();
        System.out.println(service.find());
    }

}
