package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false)
    private String cidade;
    
    @Column(nullable = false)
    private String bairro;
    
    @Column(nullable = false)
    private String rua;
    
    @Column(nullable = false)
    private String numero;
    
    @Column
    private String complemento;
    
    @Column(nullable = false)
    private String codigoPostal;
}