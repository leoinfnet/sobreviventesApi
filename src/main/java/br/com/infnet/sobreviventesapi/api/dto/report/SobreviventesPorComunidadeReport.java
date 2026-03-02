package br.com.infnet.sobreviventesapi.api.dto.report;

public record SobreviventesPorComunidadeReport(
        Long comunidadeId,
        String comunidadeNome,
        long totalSobreviventes
) {
}
