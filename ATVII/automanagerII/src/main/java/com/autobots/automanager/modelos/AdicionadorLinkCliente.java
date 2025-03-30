package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

    @Override
    public void adicionarLink(List<Cliente> lista) {
        for (Cliente cliente : lista) {
            long id = cliente.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ClienteControle.class)
                            .obterCliente(id))
                    .withSelfRel();
            cliente.add(linkProprio);
            
            
            adicionarLinksAdicionais(cliente);
        }
    }

    @Override
    public void adicionarLink(Cliente objeto) {
      
        Link linkClientes = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ClienteControle.class)
                        .obterClientes())
                .withRel("clientes");
        objeto.add(linkClientes);
        
       
        adicionarLinksAdicionais(objeto);
    }
    
    private void adicionarLinksAdicionais(Cliente cliente) {
        long id = cliente.getId();
        
        
        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ClienteControle.class)
                        .atualizarCliente(cliente))
                .withRel("update")
                .withType("PUT");
        cliente.add(linkAtualizar);
        
        
        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ClienteControle.class)
                        .excluirCliente(cliente))
                .withRel("delete")
                .withType("DELETE");
        cliente.add(linkExcluir);
        
   
        adicionarLinksRelacionados(cliente);
    }
    
    private void adicionarLinksRelacionados(Cliente cliente) {
      
        Link linkDocumentos = Link.of("/clientes/" + cliente.getId() + "/documentos")
                .withRel("documentos")
                .withType("GET");
        cliente.add(linkDocumentos);
        
       
        Link linkEndereco = Link.of("/clientes/" + cliente.getId() + "/endereco")
                .withRel("endereco")
                .withType("GET");
        cliente.add(linkEndereco);
        
        
        Link linkTelefones = Link.of("/clientes/" + cliente.getId() + "/telefones")
                .withRel("telefones")
                .withType("GET");
        cliente.add(linkTelefones);
    }
}