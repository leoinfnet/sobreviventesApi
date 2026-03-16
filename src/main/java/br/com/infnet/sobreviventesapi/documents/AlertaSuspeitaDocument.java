package br.com.infnet.sobreviventesapi.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
@Document(indexName = "alertas_suspeitas")
public class AlertaSuspeitaDocument {
    @Id
    private String alertaId;
    private String titulo;
    private String descricao;
    private String tipo;
    private String gravidade;
    private String status;
    private String regiao;
    private String localTexto;
    private Double latitude;
    private Double longitude;
    private String fonte;
    private Integer confiabilidade;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;
}
