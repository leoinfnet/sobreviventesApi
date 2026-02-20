package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recursos")
@Getter@Setter
public class Recurso {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false)
    private int quantidade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "sobrevivente_id",
            nullable = false,
            foreignKey = @ForeignKey(name =  "fk_recurso_sobrevivente")
    )
    private Sobrevivente sobrevivente;

    public Recurso(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }
    protected Recurso() {}

    public void vincularA(Sobrevivente sobrevivente) {
        this.sobrevivente = sobrevivente;
    }
}
