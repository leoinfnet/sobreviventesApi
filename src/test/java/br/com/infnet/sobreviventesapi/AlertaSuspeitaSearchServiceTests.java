package br.com.infnet.sobreviventesapi;

import br.com.infnet.sobreviventesapi.documents.AlertaSuspeitaDocument;
import br.com.infnet.sobreviventesapi.service.AlertaSuspeitaSearchService;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Profile("test")
public class AlertaSuspeitaSearchServiceTests {
    @Autowired
    private AlertaSuspeitaSearchService service;
    @Test
    void deveBuscarPorDescricao() throws Exception {

        var resultados = service.buscarPorDescricao("Drones");
        resultados.forEach(System.out::println);
        assertFalse(resultados.isEmpty());
    }
    @Test
    void deveBuscarInvestigar() throws Exception {

        var resultados = service.investigarEventoSuspeito("Drones");
        resultados.forEach(System.out::println);
        assertFalse(resultados.isEmpty());
    }
    @Test
    void deveBuscarInvestigarAno() throws Exception {

        var resultados = service.alertasPorAno();
        resultados.forEach(System.out::println);
        assertFalse(resultados.isEmpty());
    }
    @Test
    void deveBuscarInvestigarMes() throws Exception {

        var resultados = service.alertasPorMes(2019);
        resultados.forEach(System.out::println);
        assertFalse(resultados.isEmpty());
    }

}
