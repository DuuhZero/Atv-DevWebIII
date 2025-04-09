package com.autobots.automanager.controladores;

import java.util.Date;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public CollectionModel<EntityModel<Mercadoria>> listarMercadorias() {
        List<Mercadoria> mercadorias = repositorio.findAll().stream()
            .flatMap(emp -> emp.getMercadorias().stream())
            .collect(Collectors.toList());
            
        List<EntityModel<Mercadoria>> recursos = mercadorias.stream()
            .map(merc -> EntityModel.of(merc,
                linkTo(methodOn(MercadoriaControlador.class).obterMercadoria(merc.getId())).withSelfRel(),
                linkTo(methodOn(MercadoriaControlador.class).listarMercadorias()).withRel("todas-mercadorias")
            )).collect(Collectors.toList());

        return CollectionModel.of(recursos,
            linkTo(methodOn(MercadoriaControlador.class).listarMercadorias()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Mercadoria> obterMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = repositorio.findAll().stream()
            .flatMap(emp -> emp.getMercadorias().stream())
            .filter(merc -> merc.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
            
        return EntityModel.of(mercadoria,
            linkTo(methodOn(MercadoriaControlador.class).obterMercadoria(id)).withSelfRel(),
            linkTo(methodOn(MercadoriaControlador.class).listarMercadorias()).withRel("todas-mercadorias")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Mercadoria>> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        mercadoria.setCadastro(new Date());
        Empresa empresa = repositorio.findAll().get(0); // Adaptar conforme sua lógica de negócio
        empresa.getMercadorias().add(mercadoria);
        repositorio.save(empresa);
        
        EntityModel<Mercadoria> resource = EntityModel.of(mercadoria,
            linkTo(methodOn(MercadoriaControlador.class).obterMercadoria(mercadoria.getId())).withSelfRel()
        );
        return ResponseEntity.created(URI.create("/mercadorias/" + mercadoria.getId())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria atualizacao) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getMercadorias().stream().anyMatch(merc -> merc.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
            
        empresa.getMercadorias().removeIf(merc -> merc.getId().equals(id));
        atualizacao.setId(id);
        empresa.getMercadorias().add(atualizacao);
        repositorio.save(empresa);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirMercadoria(@PathVariable Long id) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getMercadorias().stream().anyMatch(merc -> merc.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
            
        empresa.getMercadorias().removeIf(merc -> merc.getId().equals(id));
        repositorio.save(empresa);
        return ResponseEntity.noContent().build();
    }
}