package br.com.infnet.sobreviventesapi.service;

import br.com.infnet.sobreviventesapi.api.dto.AlertaBuscaDTO;
import br.com.infnet.sobreviventesapi.api.dto.report.AlertasPorAno;
import br.com.infnet.sobreviventesapi.api.dto.report.AlertasPorMes;
import br.com.infnet.sobreviventesapi.api.dto.report.LocalAggDTO;
import br.com.infnet.sobreviventesapi.documents.AlertaSuspeitaDocument;
import br.com.infnet.sobreviventesapi.repository.AlertaSuspeitaSearchRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.query_dsl.*;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaSuspeitaSearchService {
    private final ElasticsearchClient client;
    private List<AlertaBuscaDTO> toDtoList(SearchResponse<AlertaSuspeitaDocument> response) {

        return response.hits()
                .hits()
                .stream()
                .map(AlertaBuscaDTO::toDto)
                .toList();
    }

    public List<AlertaBuscaDTO> buscarPorDescricao(String termo) throws IOException {

        SearchResponse<AlertaSuspeitaDocument> response =
                client.search(s -> s
                                .index("alertas_suspeitos")
                                .query(q -> q
                                        .match(m -> m
                                                .field("descricao")
                                                .query(termo)
                                        )
                                ),
                        AlertaSuspeitaDocument.class
                );

        return toDtoList(response);
    }
    public List<AlertaBuscaDTO> buscarTexto(String termo) throws IOException {

        SearchResponse<AlertaSuspeitaDocument> response =
                client.search(s -> s
                                .index("alertas_suspeitos")
                                .query(q -> q
                                        .multiMatch(m -> m
                                                .query(termo)
                                                .fields("titulo^2", "descricao", "local")
                                        )
                                ),
                        AlertaSuspeitaDocument.class
                );

        return toDtoList(response);
    }

    public List<AlertaBuscaDTO> buscarPorPeriodo(String inicio, String fim) throws IOException {

        SearchResponse<AlertaSuspeitaDocument> response =
                client.search(s -> s
                                .index("alertas_suspeitos")
                                .query(q -> q
                                        .range(r -> r
                                                .date(n -> n
                                                        .field("dataHora")
                                                        .gte(inicio)
                                                        .lte(fim)
                                                )
                                        )
                                ),
                        AlertaSuspeitaDocument.class
                );

        return toDtoList(response);
    }
    public List<AlertaBuscaDTO> buscarFuzzy(String termo) throws IOException {

        SearchResponse<AlertaSuspeitaDocument> response =
                client.search(s -> s
                                .index("alertas_suspeitos")
                                .query(q -> q
                                        .match(m -> m
                                                .field("descricao")
                                                .query(termo)
                                                .fuzziness("AUTO")
                                        )
                                ),
                        AlertaSuspeitaDocument.class
                );

        return toDtoList(response);
    }


    public List<AlertaBuscaDTO> investigarEventoSuspeito(String termo) throws IOException {

        SearchResponse<AlertaSuspeitaDocument> response =
                client.search(s -> s
                                .index("alertas_suspeitas")
                                .query(q -> q
                                        .bool(b -> b

                                                // texto obrigatório
                                                .must(m -> m
                                                        .match(t -> t
                                                                .field("descricao")
                                                                .query(termo)
                                                        )
                                                )

                                                // filtro temporal (não afeta score)
                                                .filter(f -> f
                                                        .range(r -> r
                                                                .date(d -> d
                                                                        .field("dataHora")
                                                                        .gte(LocalDateTime.now().minusYears(1).toString())
                                                                )
                                                        )
                                                )
                                                // excluir locais comuns
                                                .mustNot(n -> n
                                                        .term(t -> t
                                                                .field("localText.keyword")
                                                                .value("Aeroporto")
                                                        )
                                                )

                                                // priorizar locais estratégicos
                                                .should(s1 -> s1
                                                        .term(t -> t
                                                                .field("localText.keyword")
                                                                .value("Hospital Abandonado")
                                                        )
                                                )
                                                .should(s2 -> s2
                                                        .term(t -> t
                                                                .field("localTexto.keyword")
                                                                .value("Torre de Radio")
                                                        )
                                                )

                                        )
                                ),
                        AlertaSuspeitaDocument.class
                );

        return response.hits()
                .hits()
                .stream()
                .map(AlertaBuscaDTO::toDto)
                .toList();
    }
    public List<LocalAggDTO> alertasPorLocal() throws IOException {

        SearchResponse<Void> response = client.search(s -> s
                        .index("alertas_suspeitas")
                        .size(0) // não queremos documentos, só agregação
                        .aggregations("por_local", a -> a
                                .terms(t -> t
                                        .field("localTexto.keyword")
                                        .size(10)
                                )
                        ),
                Void.class
        );

        List<LocalAggDTO> resultado = new ArrayList<>();

        var buckets = response.aggregations()
                .get("por_local")
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            resultado.add(
                    new LocalAggDTO(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    )
            );
        }

        return resultado;
    }
    public List<AlertasPorAno> alertasPorAno() throws IOException {

        SearchResponse<Void> response = client.search(s -> s
                        .index("alertas_suspeitas")
                        .size(0)
                        .aggregations("alertas_por_ano", a -> a
                                .dateHistogram(d -> d
                                        .field("dataHora")
                                        .calendarInterval(CalendarInterval.Year)
                                )
                        ),
                Void.class
        );

        return response.aggregations()
                .get("alertas_por_ano")
                .dateHistogram()
                .buckets()
                .array()
                .stream()
                .map(bucket -> new AlertasPorAno(
                       getYear(bucket.keyAsString()),
                        bucket.docCount()
                ))
                .toList();
    }

    public List<AlertasPorMes> alertasPorMes(int ano) throws IOException {

        String inicio = inicioAno(ano);
        String fim = inicioProximoAno(ano);

        SearchResponse<Void> response = client.search(s -> s
                        .index("alertas_suspeitas")
                        .size(0)

                        .query(q -> q
                                .range(r -> r
                                        .date(n -> n
                                                .field("dataHora")
                                                .gte(inicio)
                                                .lt(fim)
                                        )
                                )
                        )

                        .aggregations("alertas_por_mes", a -> a
                                .dateHistogram(d -> d
                                        .field("dataHora")
                                        .calendarInterval(CalendarInterval.Month)
                                )
                        ),

                Void.class
        );

        return response.aggregations()
                .get("alertas_por_mes")
                .dateHistogram()
                .buckets()
                .array()
                .stream()
                .map(bucket -> new AlertasPorMes(
                        bucket.keyAsString(),
                        bucket.docCount()
                ))
                .toList();
    }



    private String getYear(String data){
        return Instant
                .parse(data)
                .atZone(ZoneOffset.UTC)
                .getYear() + "";

    }


    private String inicioAno(int ano) {
        return LocalDate.of(ano, 1, 1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toString();
    }

    private String inicioProximoAno(int ano) {
        return LocalDate.of(ano + 1, 1, 1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toString();
    }

}
