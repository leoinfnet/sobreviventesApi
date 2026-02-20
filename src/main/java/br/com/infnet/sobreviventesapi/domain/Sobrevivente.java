package br.com.infnet.sobreviventesapi.service;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;

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


}
