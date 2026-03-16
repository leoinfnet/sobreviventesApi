package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;


@Entity
@Getter@Setter
@Immutable
public class Leaderboard implements Serializable {
    @Id
    @Column(name = "survivor_id")
    private Long id;
    @Column(name = "total_score")
    private Long totalScore;

}
