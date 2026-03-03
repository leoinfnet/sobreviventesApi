package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter@Setter
public class SurvivorActivityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "survivor_activity_event_seq")
    @SequenceGenerator(
            name = "survivor_activity_event_seq",
            sequenceName = "survivor_activity_event_seq",
            allocationSize = 50
    )
    private Long id;

    // Quem gerou o evento (chave de agregação principal)
    @Column(name = "survivor_id", nullable = false)
    private Long survivorId;

    // Quando aconteceu (base para janelas: 7d/30d/season)
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    // Tipo do evento (define como pontua)
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private EventType eventType;

    // Um "valor" numérico padrão para facilitar agregações:
    // ex: ameaça=10, resgate=30, trade=5, missão=20...
    @Column(name = "score_value", nullable = false)
    private Integer scoreValue;

    // Dimensão comum para filtrar ranking (opcional, mas útil)
    @Column(name = "region", length = 40)
    private String region;

    // Para deduplicação e rastreabilidade (id do evento no domínio)
    @Column(name = "event_key", nullable = false, unique = true, length = 80)
    private String eventKey;

    // “Motivo” humano / debug (não use isso no ranking, mas ajuda muito)
    @Column(name = "description", length = 255)
    private String description;

    protected SurvivorActivityEvent() {}

    public SurvivorActivityEvent(Long survivorId, Instant occurredAt, EventType eventType,
                                 Integer scoreValue, String region, String eventKey, String description) {
        this.survivorId = survivorId;
        this.occurredAt = occurredAt;
        this.eventType = eventType;
        this.scoreValue = scoreValue;
        this.region = region;
        this.eventKey = eventKey;
        this.description = description;
    }

    public enum EventType {
        THREAT_DEFEATED,
        SUPPLY_FOUND,
        MISSION_COMPLETED,
        TRADE_COMPLETED,
        ALLY_RESCUED,
        BASE_UPGRADED
    }
}
