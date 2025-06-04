package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Vendedor;
import com.autobots.automanager.repositorios.RepositorioVendedor;

@RestController
@RequestMapping("/vendedores")
public class ControleVendedor {

    @Autowired
    private RepositorioVendedor repositorio;

    @GetMapping("/listar")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Vendedor>> listarVendedores() {
        List<Vendedor> vendedores = repositorio.findAll();
        if (vendedores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(vendedores, HttpStatus.OK);
    }

    @PostMapping("/criar")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> criarVendedor(@RequestBody Vendedor vendedor) {
        repositorio.save(vendedor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> atualizarVendedor(@PathVariable Long id, @RequestBody Vendedor vendedorAtualizado) {
        Optional<Vendedor> vendedor = repositorio.findById(id);
        if (vendedor.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Vendedor v = vendedor.get();
        v.setNome(vendedorAtualizado.getNome());
        v.setEmail(vendedorAtualizado.getEmail());
        v.setTelefone(vendedorAtualizado.getTelefone());

        repositorio.save(v);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> deletarVendedor(@PathVariable Long id) {
        Optional<Vendedor> vendedor = repositorio.findById(id);
        if (vendedor.isPresent()) {
            repositorio.delete(vendedor.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
