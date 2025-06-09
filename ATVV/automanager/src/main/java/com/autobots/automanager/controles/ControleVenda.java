package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/vendas")
public class ControleVenda extends ControleHATEOAS<Venda> {

    @Autowired
    private RepositorioVenda repositorio;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
        repositorio.save(venda);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('VENDEDOR')")
    @GetMapping("/minhas-vendas")
    public ResponseEntity<List<Venda>> listarMinhasVendas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Venda> vendas = repositorio.findByFuncionarioCredencialNomeUsuario(username);
        return configurarResposta(vendas, this.getClass());
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/minhas-compras")
    public ResponseEntity<List<Venda>> listarMinhasCompras() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Venda> compras = repositorio.findByClienteCredencialNomeUsuario(username);
        return configurarResposta(compras, this.getClass());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/listar")
    public ResponseEntity<List<Venda>> listarTodasVendas() {
        List<Venda> vendas = repositorio.findAll();
        return configurarResposta(vendas, this.getClass());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarVenda(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarVenda(@PathVariable Long id, @RequestBody Venda vendaAtualizada) {
        if (repositorio.existsById(id)) {
            Venda vendaExistente = repositorio.findById(id).orElse(null);
            if (vendaExistente != null) {
                vendaAtualizada.setId(vendaExistente.getId());
                repositorio.save(vendaAtualizada);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}