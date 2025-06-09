package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuarios")
public class ControleUsuario extends ControleHATEOAS<Usuario> {

    @Autowired
    private RepositorioUsuario repositorio;

    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = repositorio.findAll();
        return configurarResposta(usuarios, this.getClass());
    }

    @GetMapping("/buscar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = repositorio.findById(id);
        return usuario.map(value -> configurarResposta(value, this.getClass()))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
        usuario.getCredencial().setSenha(codificador.encode(usuario.getCredencial().getSenha()));
        repositorio.save(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}