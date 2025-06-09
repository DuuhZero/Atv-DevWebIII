package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servicos")
public class ControleServico extends ControleHATEOAS<Servico> {

    @Autowired
    private RepositorioServico repositorio;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico) {
        repositorio.save(servico);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> servicos = repositorio.findAll();
        return configurarResposta(servicos, this.getClass());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Servico> buscarServico(@PathVariable Long id) {
        Servico servico = repositorio.findById(id).orElse(null);
        return configurarResposta(servico, this.getClass());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
        if (repositorio.existsById(id)) {
            Servico servicoExistente = repositorio.findById(id).orElse(null);
            if (servicoExistente != null) {
                servicoAtualizado.setId(servicoExistente.getId());
                repositorio.save(servicoAtualizado);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}