package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_suspeitas", schema = "operacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaSuspeita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alerta_id", nullable = false, unique = true)
    private String alertaId;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String tipo;

    private String gravidade;

    private String status;

    private String regiao;

    @Column(name = "local_texto")
    private String localTexto;

    private Double latitude;

    private Double longitude;

    private String fonte;

    private Integer confiabilidade;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "palavras_chave", columnDefinition = "jsonb")
    private String palavrasChave;

    @Column(name = "sobreviventes_relacionados", columnDefinition = "jsonb")
    private String sobreviventesRelacionados;

    @Column(name = "comunidades_relacionadas", columnDefinition = "jsonb")
    private String comunidadesRelacionadas;

    @Column(name = "responsavel_registro")
    private String responsavelRegistro;

    @Column(name = "observacoes_internas", columnDefinition = "TEXT")
    private String observacoesInternas;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
