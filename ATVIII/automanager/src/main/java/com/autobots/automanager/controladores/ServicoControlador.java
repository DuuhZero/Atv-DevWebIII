package com.autobots.automanager.controladores;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/servicos")
public class ServicoControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public CollectionModel<EntityModel<Servico>> listarServicos() {
        List<Servico> servicos = repositorio.findAll().stream()
            .flatMap(emp -> emp.getServicos().stream())
            .collect(Collectors.toList());
            
        List<EntityModel<Servico>> recursos = servicos.stream()
            .map(serv -> EntityModel.of(serv,
                linkTo(methodOn(ServicoControlador.class).obterServico(serv.getId())).withSelfRel(),
                linkTo(methodOn(ServicoControlador.class).listarServicos()).withRel("todos-servicos")
            )).collect(Collectors.toList());

        return CollectionModel.of(recursos,
            linkTo(methodOn(ServicoControlador.class).listarServicos()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Servico> obterServico(@PathVariable Long id) {
        Servico servico = repositorio.findAll().stream()
            .flatMap(emp -> emp.getServicos().stream())
            .filter(serv -> serv.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            
        return EntityModel.of(servico,
            linkTo(methodOn(ServicoControlador.class).obterServico(id)).withSelfRel(),
            linkTo(methodOn(ServicoControlador.class).listarServicos()).withRel("todos-servicos")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Servico>> cadastrarServico(@RequestBody Servico servico) {
        Empresa empresa = repositorio.findAll().get(0); 
        empresa.getServicos().add(servico);
        repositorio.save(empresa);
        
        EntityModel<Servico> resource = EntityModel.of(servico,
            linkTo(methodOn(ServicoControlador.class).obterServico(servico.getId())).withSelfRel()
        );
        return ResponseEntity.created(URI.create("/servicos/" + servico.getId())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarServico(@PathVariable Long id, @RequestBody Servico atualizacao) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getServicos().stream().anyMatch(serv -> serv.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            
        empresa.getServicos().removeIf(serv -> serv.getId().equals(id));
        atualizacao.setId(id);
        empresa.getServicos().add(atualizacao);
        repositorio.save(empresa);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirServico(@PathVariable Long id) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getServicos().stream().anyMatch(serv -> serv.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
            
        empresa.getServicos().removeIf(serv -> serv.getId().equals(id));
        repositorio.save(empresa);
        return ResponseEntity.noContent().build();
    }
}