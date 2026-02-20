package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity@Table(name = "comunidades")
@Getter@Setter
public class Comunidade {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;
    @Column(nullable = false)
    private boolean zonaSegura;

    @ManyToMany(mappedBy = "comunidades")
    private Set<Sobrevivente> membros = new HashSet<>();


    public void adicionarMembro(Sobrevivente sobrevivente) {
        this.membros.add(sobrevivente);
    }
}
