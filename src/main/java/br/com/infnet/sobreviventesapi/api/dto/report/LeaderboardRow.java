package br.com.infnet.sobreviventesapi.api.dto.report;

import java.io.Serializable;

public record LeaderboardRow(
        Long suvivorId,
        Long totalScore
) implements Serializable {
}
