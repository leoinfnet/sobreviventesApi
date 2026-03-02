package br.com.infnet.sobreviventesapi;

import br.com.infnet.sobreviventesapi.api.dto.report.LeaderboardRow;
import br.com.infnet.sobreviventesapi.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class LeaderboardServiceTests {
    @Autowired
    private LeaderboardService service;
    @Test
    void printTop10LastWeek() {

        List<LeaderboardRow> ranking = service.top10LastWeek();

        System.out.println("=== TOP 10 LAST WEEK ===");

        ranking.forEach(row ->
                System.out.println(
                        "Survivor: " + row.suvivorId() +
                                " | Score: " + row.totalScore()
                )
        );

        System.out.println("========================");
    }
}
