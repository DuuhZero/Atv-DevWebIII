package com.autobots.automanager.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Empresa extends RepresentationModel<Empresa> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String razaoSocial;

    @Column
    private String nomeFantasia;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @Column(nullable = false)
    private Date cadastro;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private Set<Usuario> usuarios = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Mercadoria> mercadorias = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Servico> servicos = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Venda> vendas = new HashSet<>();
}