package br.com.infnet.sobreviventesapi.api.dto;

import java.util.List;

public record SobreviventeSimples(
        Long id,
        String nome,
        String localizacao,
        boolean infectado

) {
}
