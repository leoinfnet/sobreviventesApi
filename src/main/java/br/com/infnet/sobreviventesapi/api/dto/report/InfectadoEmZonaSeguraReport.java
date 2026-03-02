package br.com.infnet.sobreviventesapi.api.dto.report;

public record InfectadoEmZonaSeguraReport(
        Long sobreviventeId,
        String nome,
        String comunidadeNome
) {
}
