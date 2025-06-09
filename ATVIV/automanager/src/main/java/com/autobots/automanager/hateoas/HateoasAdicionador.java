package com.autobots.automanager.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HateoasAdicionador {
    
    public void adicionarLink(Object objeto, Class<?> controlador, String rel) {
        if (objeto instanceof List<?>) {
            ((List<?>) objeto).forEach(item -> adicionarLinkIndividual(item, controlador, rel));
        } else {
            adicionarLinkIndividual(objeto, controlador, rel);
        }
    }

    private void adicionarLinkIndividual(Object objeto, Class<?> controlador, String rel) {
        try {
            Long id = (Long) objeto.getClass().getMethod("getId").invoke(objeto);
            String nomeRecurso = controlador.getSimpleName().replace("Controle", "").toLowerCase();
            
           
            Link linkRecurso = Link.of(
                String.format("http://localhost:8080/%s/%d", nomeRecurso, id)
            ).withRel(rel);
            
            objeto.getClass().getMethod("add", Link.class).invoke(objeto, linkRecurso);
            
            
            Link linkTodos = Link.of(
                String.format("http://localhost:8080/%s", nomeRecurso)
            ).withRel("todos_" + nomeRecurso);
            
            objeto.getClass().getMethod("add", Link.class).invoke(objeto, linkTodos);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}