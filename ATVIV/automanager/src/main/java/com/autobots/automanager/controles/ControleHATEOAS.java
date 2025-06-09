package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.autobots.automanager.hateoas.HateoasAdicionador;

import java.util.List;

public abstract class ControleHATEOAS<T> {
    
    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    protected ResponseEntity<T> configurarResposta(T entidade, Class<?> controlador) {
        if (entidade == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        hateoasAdicionador.adicionarLink(entidade, controlador, "self");
        return new ResponseEntity<>(entidade, HttpStatus.OK);
    }

    protected ResponseEntity<List<T>> configurarResposta(List<T> entidades, Class<?> controlador) {
        if (entidades.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        hateoasAdicionador.adicionarLink(entidades, controlador, "self");
        return new ResponseEntity<>(entidades, HttpStatus.OK);
    }
}