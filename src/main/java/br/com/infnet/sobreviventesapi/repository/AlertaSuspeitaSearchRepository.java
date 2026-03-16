package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.documents.AlertaSuspeitaDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AlertaSuspeitaSearchRepository   extends
        ElasticsearchRepository<AlertaSuspeitaDocument, String> {
    List<AlertaSuspeitaDocument> findByTipo(String tipo);

    List<AlertaSuspeitaDocument> findByGravidade(String gravidade);

    List<AlertaSuspeitaDocument> findByRegiao(String regiao);

}
