package br.com.infnet.sobreviventesapi.api.controller;

import br.com.infnet.sobreviventesapi.api.dto.report.LeaderboardRow;
import br.com.infnet.sobreviventesapi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService service;
    /**
     * Exemplo:
     * GET /leaderboard
     * GET /leaderboard?top=10
     */
    @GetMapping
    public ResponseEntity<List<LeaderboardRow>> topLastWeek(
            @RequestParam(name = "top", defaultValue = "10") int top
    ) {
        // proteção básica pra não deixar alguém pedir top=999999 e derrubar o mundo
        return null;
    }
}
