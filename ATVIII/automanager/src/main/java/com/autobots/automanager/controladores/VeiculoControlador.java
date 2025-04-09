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
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/veiculos")
public class VeiculoControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public CollectionModel<EntityModel<Veiculo>> listarVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll().stream()
            .flatMap(emp -> emp.getUsuarios().stream())
            .flatMap(user -> user.getVeiculos().stream())
            .collect(Collectors.toList());
            
        List<EntityModel<Veiculo>> recursos = veiculos.stream()
            .map(veiculo -> EntityModel.of(veiculo,
                linkTo(methodOn(VeiculoControlador.class).obterVeiculo(veiculo.getId())).withSelfRel(),
                linkTo(methodOn(VeiculoControlador.class).listarVeiculos()).withRel("todos-veiculos")
            )).collect(Collectors.toList());

        return CollectionModel.of(recursos,
            linkTo(methodOn(VeiculoControlador.class).listarVeiculos()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Veiculo> obterVeiculo(@PathVariable Long id) {
        Veiculo veiculo = repositorio.findAll().stream()
            .flatMap(emp -> emp.getUsuarios().stream())
            .flatMap(user -> user.getVeiculos().stream())
            .filter(veic -> veic.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            
        return EntityModel.of(veiculo,
            linkTo(methodOn(VeiculoControlador.class).obterVeiculo(id)).withSelfRel(),
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(veiculo.getProprietario().getId())).withRel("proprietario"),
            linkTo(methodOn(VeiculoControlador.class).listarVeiculos()).withRel("todos-veiculos")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Veiculo>> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        Usuario usuario = repositorio.findAll().stream()
            .flatMap(emp -> emp.getUsuarios().stream())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nenhum usuário encontrado"));
            
        usuario.getVeiculos().add(veiculo);
        repositorio.saveAll(repositorio.findAll()); 
        
        EntityModel<Veiculo> resource = EntityModel.of(veiculo,
            linkTo(methodOn(VeiculoControlador.class).obterVeiculo(veiculo.getId())).withSelfRel()
        );
        return ResponseEntity.created(URI.create("/veiculos/" + veiculo.getId())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo atualizacao) {
        List<Empresa> empresas = repositorio.findAll();
        empresas.forEach(emp -> 
            emp.getUsuarios().forEach(user -> {
                user.getVeiculos().removeIf(veic -> veic.getId().equals(id));
                if (user.getId().equals(atualizacao.getProprietario().getId())) {
                    atualizacao.setId(id);
                    user.getVeiculos().add(atualizacao);
                }
            })
        );
        repositorio.saveAll(empresas);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
        List<Empresa> empresas = repositorio.findAll();
        empresas.forEach(emp -> 
            emp.getUsuarios().forEach(user -> 
                user.getVeiculos().removeIf(veic -> veic.getId().equals(id))
            )
        );
        repositorio.saveAll(empresas);
        return ResponseEntity.noContent().build();
    }
}