package br.com.infnet.sobreviventesapi.domain;

import jakarta.persistence.*;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "sobreviventes",
    uniqueConstraints = {@UniqueConstraint(name = "uk_sobrevivente_nome",
            columnNames = "nome")})
@Getter@Setter
public class Sobrevivente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String nome;
    @Column(nullable = false,length = 80)
    private String localizacao;
    @Column(nullable = false)
    private boolean infectado = false;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<Recurso> recursos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sobrevivente_comunidade",
            joinColumns = @JoinColumn(
                    nullable = false,
                    name = "sobrevivente_id"
            ),
            inverseJoinColumns = @JoinColumn(name = "comunidade_id",
                    nullable = false)
    )
    private Set<Comunidade> comunidades = new HashSet<>();
    protected  Sobrevivente() {}
    public Sobrevivente(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public void marcarComoInfectado() {
        this.infectado = true;
    }
    public void adicionarRecurso(String nome,int quantidade) {
        Recurso recurso = new Recurso(nome, quantidade);
        this.recursos.add(recurso);
        recurso.vincularA(this);
    }
    public void removerRecurso(Long recursoId) {
        recursos.removeIf(recurso -> Objects.equals(recurso.getId(), recursoId));
    }
    public void entrarNaComunidade(Comunidade comunidade) {
        if(this.infectado && comunidade.isZonaSegura()){
            throw new IllegalArgumentException("Infectado não pode entrar em zona segura");
        }
        this.comunidades.add(comunidade);
        comunidade.adicionarMembro(this);
    }

}
