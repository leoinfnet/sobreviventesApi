package br.com.infnet.sobreviventesapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarSobreviventeResquest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        @NotBlank(message = "Localização é obrigatória")
        @Size(min = 2, max = 100, message = "Localização deve ter entre 2 e 100 caracteres")
        String localizacao
) {
}
