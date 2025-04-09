package com.autobots.automanager.controladores;

import java.util.Date;
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
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/vendas")
public class VendaControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public CollectionModel<EntityModel<Venda>> listarVendas() {
        List<Venda> vendas = repositorio.findAll().stream()
            .flatMap(emp -> emp.getVendas().stream())
            .collect(Collectors.toList());
            
        List<EntityModel<Venda>> recursos = vendas.stream()
            .map(venda -> EntityModel.of(venda,
                linkTo(methodOn(VendaControlador.class).obterVenda(venda.getId())).withSelfRel(),
                linkTo(methodOn(VendaControlador.class).listarVendas()).withRel("todas-vendas")
            )).collect(Collectors.toList());

        return CollectionModel.of(recursos,
            linkTo(methodOn(VendaControlador.class).listarVendas()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Venda> obterVenda(@PathVariable Long id) {
        Venda venda = repositorio.findAll().stream()
            .flatMap(emp -> emp.getVendas().stream())
            .filter(vend -> vend.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
            
        return EntityModel.of(venda,
            linkTo(methodOn(VendaControlador.class).obterVenda(id)).withSelfRel(),
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(venda.getCliente().getId())).withRel("cliente"),
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(venda.getFuncionario().getId())).withRel("funcionario"),
            linkTo(methodOn(VeiculoControlador.class).obterVeiculo(venda.getVeiculo().getId())).withRel("veiculo"),
            linkTo(methodOn(VendaControlador.class).listarVendas()).withRel("todas-vendas")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venda>> cadastrarVenda(@RequestBody Venda venda) {
        venda.setCadastro(new Date());
        Empresa empresa = repositorio.findAll().get(0); 
        empresa.getVendas().add(venda);
        repositorio.save(empresa);
        
        EntityModel<Venda> resource = EntityModel.of(venda,
            linkTo(methodOn(VendaControlador.class).obterVenda(venda.getId())).withSelfRel()
        );
        return ResponseEntity.created(URI.create("/vendas/" + venda.getId())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVenda(@PathVariable Long id, @RequestBody Venda atualizacao) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getVendas().stream().anyMatch(vend -> vend.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
            
        empresa.getVendas().removeIf(vend -> vend.getId().equals(id));
        atualizacao.setId(id);
        empresa.getVendas().add(atualizacao);
        repositorio.save(empresa);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getVendas().stream().anyMatch(vend -> vend.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
            
        empresa.getVendas().removeIf(vend -> vend.getId().equals(id));
        repositorio.save(empresa);
        return ResponseEntity.noContent().build();
    }
}