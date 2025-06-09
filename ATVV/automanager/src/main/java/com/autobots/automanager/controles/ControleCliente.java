package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.RepositorioCliente;

@RestController
@RequestMapping("/clientes")
public class ControleCliente extends ControleHATEOAS<Cliente> {

    @Autowired
    private RepositorioCliente repositorio;

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = repositorio.findAll();
        return configurarResposta(clientes, this.getClass());
    }

    @GetMapping("/buscar/{id}")
    @PreAuthorize("hasRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable Long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        return configurarResposta(cliente, this.getClass());
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    public ResponseEntity<?> criarCliente(@RequestBody Cliente cliente) {
        repositorio.save(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/atualizar/{id}")
    @PreAuthorize("hasRole('ADMIN', 'GERENTE')")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        if (repositorio.existsById(id)) {
            Cliente clienteExistente = repositorio.findById(id).orElse(null);
            if (clienteExistente != null) {
                clienteAtualizado.setId(clienteExistente.getId());
                repositorio.save(clienteAtualizado);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}