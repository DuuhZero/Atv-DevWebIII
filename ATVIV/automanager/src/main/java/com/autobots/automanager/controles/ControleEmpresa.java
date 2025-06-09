package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/empresas")
public class ControleEmpresa extends ControleHATEOAS<Empresa> {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @PostMapping("/associar/{idEmpresa}")
    public ResponseEntity<?> associarUsuario(@PathVariable Long idEmpresa, @RequestBody Map<String, Long> request) {
        Long idUsuario = request.get("idUsuario");

        Empresa empresa = repositorio.findById(idEmpresa).orElse(null);
        Usuario usuario = repositorioUsuario.findById(idUsuario).orElse(null);

        if (empresa == null || usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa ou usuário não encontrado");
        }

        empresa.getUsuarios().add(usuario);
        repositorio.save(empresa);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        return configurarResposta(empresas, this.getClass());
    }

    @GetMapping("/buscar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Empresa> buscarEmpresa(@PathVariable Long id) {
        Empresa empresa = repositorio.findById(id).orElse(null);
        return configurarResposta(empresa, this.getClass());
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarEmpresa(@RequestBody Empresa empresa) {
        empresa.setCadastro(new Date());
        repositorio.save(empresa);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresaAtualizada) {
        if (repositorio.existsById(id)) {
            empresaAtualizada.setId(id);
            repositorio.save(empresaAtualizada);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}