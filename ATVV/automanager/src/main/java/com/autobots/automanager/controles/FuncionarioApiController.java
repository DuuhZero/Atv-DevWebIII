package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.hateoas.HateoasAdicionador;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioApiController {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Usuario>> obterFuncionariosPorEmpresa(@PathVariable Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Usuario> funcionarios = empresa.getUsuarios().stream()
            .filter(usuario -> !usuario.getPerfis().contains(Perfil.CLIENTE))
            .collect(Collectors.toList());
            
        hateoasAdicionador.adicionarLink(funcionarios, ControleUsuario.class, "self");
        
        return ResponseEntity.ok(funcionarios);
    }
}