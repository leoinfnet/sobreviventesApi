package br.com.infnet.sobreviventesapi.api.dto;

import java.util.List;
import java.util.Set;

public record SobreviventeResponse(
        Long id,
        String nome,
        String localizacao,
        boolean infectado,
        List<RecursoResponse> recursos,
        Set<ComunidadeResponse> comunidades
) {
}
