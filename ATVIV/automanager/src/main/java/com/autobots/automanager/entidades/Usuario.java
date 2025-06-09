package com.autobots.automanager.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.modelos.Perfil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String nome;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Credencial credencial;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Perfil> perfis = new ArrayList<>();
}