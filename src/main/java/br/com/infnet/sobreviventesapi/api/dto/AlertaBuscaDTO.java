package br.com.infnet.sobreviventesapi.api.dto;

import br.com.infnet.sobreviventesapi.documents.AlertaSuspeitaDocument;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.time.LocalDateTime;

public record AlertaBuscaDTO(
        String id,
        String titulo,
        String descricao,
        String local,
        LocalDateTime ano,
        Double score
) {
    public static AlertaBuscaDTO toDto(Hit<AlertaSuspeitaDocument> hit) {

        AlertaSuspeitaDocument doc = hit.source();

        return new AlertaBuscaDTO(
                doc.getAlertaId(),
                doc.getTitulo(),
                doc.getDescricao(),
                doc.getLocalTexto(),
                doc.getDataHora(),
                hit.score()
        );
    }
}
