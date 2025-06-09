package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.hateoas.HateoasAdicionador;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/api/clientes")
public class ClienteApiController {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Cliente>> obterClientesPorEmpresa(@PathVariable Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Cliente> clientes = empresa.getUsuarios().stream()
            .filter(usuario -> usuario.getPerfis().contains(Perfil.CLIENTE))
            .map(usuario -> (Cliente) usuario)
            .toList();
            
        
        hateoasAdicionador.adicionarLink(clientes, ControleCliente.class, "self");
        
        return ResponseEntity.ok(clientes);
    }
}