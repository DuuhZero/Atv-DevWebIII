package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateoas.HateoasAdicionador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/api/vendas")
public class VendaApiController {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/empresa/{empresaId}/periodo")
    public ResponseEntity<List<Venda>> obterVendasPorPeriodo(
            @PathVariable Long empresaId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fim) {
        
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        
        List<Venda> vendas = empresa.getVendas().stream()
            .filter(venda -> !venda.getCadastro().before(inicio) && !venda.getCadastro().after(fim))
            .collect(Collectors.toList());
            
        hateoasAdicionador.adicionarLink(vendas, ControleVenda.class, "self");
        
        return ResponseEntity.ok(vendas);
    }
}