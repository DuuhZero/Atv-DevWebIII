package com.autobots.automanager.controles;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateoas.HateoasAdicionador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoApiController {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Set<Veiculo>> obterVeiculosPorEmpresa(@PathVariable Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        
        Set<Veiculo> veiculos = new HashSet<>();
        for (Venda venda : empresa.getVendas()) {
            if (venda.getVeiculo() != null) {
                veiculos.add(venda.getVeiculo());
            }
        }
        
        hateoasAdicionador.adicionarLink(veiculos, ControleCliente.class, "self");
        
        return ResponseEntity.ok(veiculos);
    }
}