package br.com.infnet.sobreviventesapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarComunidadeRequest(@NotBlank(message = "Nome é obrigatório")
                                          @Size(min = 2, max = 120, message = "Nome deve ter entre 2 e 120 caracteres")
                                          String nome,

                                     boolean zonaSegura) {
}
