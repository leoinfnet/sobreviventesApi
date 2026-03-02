package br.com.infnet.sobreviventesapi.api.dto.report;

public record EstoquePorRecursoReport(
        String recursoNome,
        long quantidadeTotal
) {
}
