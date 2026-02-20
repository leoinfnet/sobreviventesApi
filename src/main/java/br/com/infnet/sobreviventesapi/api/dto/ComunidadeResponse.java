package br.com.infnet.sobreviventesapi.api.dto;

public record ComunidadeResponse(
        Long id,
        String nome,
        boolean zonaSegura) {
}
