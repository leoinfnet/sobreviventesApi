package br.com.infnet.sobreviventesapi.api.dto.report;

public record TopSobreviventeRecursosReport(
        Long sobreviventeId,
        String nome,
        long totalRecursos
) {
}
