package br.com.infnet.sobreviventesapi.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdicionarRecursoRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        @Min(value = 1, message = "Quantidade deve ser maior que zero")
        int quantidade

) {
}
