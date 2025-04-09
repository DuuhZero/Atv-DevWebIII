package com.autobots.automanager.controladores;

import java.net.URI;
import java.util.List;
import java.util.Map;
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
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<EntityModel<Usuario>> recursos = repositorio.findAll().stream()
            .flatMap(empresa -> empresa.getUsuarios().stream()
                .map(usuario -> {
                    EntityModel<Usuario> resource = EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioControlador.class).obterUsuario(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withRel("todos-usuarios")
                    );
                    
                    resource.add(linkTo(methodOn(EmpresaControlador.class)
                        .obterEmpresa(empresa.getId()))
                        .withRel("empresa-associada"));
                    
                    return resource;
                }))
            .collect(Collectors.toList());

        return CollectionModel.of(recursos,
            linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Usuario> obterUsuario(@PathVariable Long id) {
        Empresa empresaAssociada = repositorio.findAll().stream()
            .filter(emp -> emp.getUsuarios().stream().anyMatch(u -> u.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Usuario usuario = empresaAssociada.getUsuarios().stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .get();
        
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(id)).withSelfRel(),
            linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withRel("todos-usuarios"),
            linkTo(methodOn(EmpresaControlador.class).obterEmpresa(empresaAssociada.getId()))
                .withRel("empresa-associada")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@RequestBody Usuario usuario) {
        Empresa empresa = repositorio.findAll().get(0);
        empresa.getUsuarios().add(usuario);
        repositorio.save(empresa);
        
        EntityModel<Usuario> resource = EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(usuario.getId())).withSelfRel()
        );
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId())).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario atualizacao) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getUsuarios().stream().anyMatch(user -> user.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        empresa.getUsuarios().removeIf(user -> user.getId().equals(id));
        atualizacao.setId(id);
        empresa.getUsuarios().add(atualizacao);
        repositorio.save(empresa);
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        Empresa empresa = repositorio.findAll().stream()
            .filter(emp -> emp.getUsuarios().stream().anyMatch(user -> user.getId().equals(id)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
        empresa.getUsuarios().removeIf(user -> user.getId().equals(id));
        repositorio.save(empresa);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{empresaId}/associar")
    public ResponseEntity<EntityModel<Usuario>> associarUsuarioEmpresa(
        @PathVariable Long empresaId, 
        @RequestBody Map<String, Long> request
    ) {
        Long usuarioId = request.get("id");
        
        Empresa empresaAtual = repositorio.findAll().stream()
            .filter(emp -> emp.getUsuarios().stream().anyMatch(u -> u.getId().equals(usuarioId)))
            .findFirst()
            .orElse(null);
        

        Usuario usuario = empresaAtual != null ? 
            empresaAtual.getUsuarios().stream()
                .filter(u -> u.getId().equals(usuarioId))
                .findFirst().get() :
            repositorio.findAll().stream()
                .flatMap(emp -> emp.getUsuarios().stream())
                .filter(u -> u.getId().equals(usuarioId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        

        Empresa novaEmpresa = repositorio.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        if (empresaAtual != null) {
            empresaAtual.getUsuarios().removeIf(u -> u.getId().equals(usuarioId));
            repositorio.save(empresaAtual);
        }

        novaEmpresa.getUsuarios().add(usuario);
        repositorio.save(novaEmpresa);
        
        return ResponseEntity.ok(EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControlador.class).obterUsuario(usuarioId)).withSelfRel(),
            linkTo(methodOn(EmpresaControlador.class).obterEmpresa(empresaId)).withRel("empresa-associada"),
            linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withRel("todos-usuarios")
        ));
    }
}