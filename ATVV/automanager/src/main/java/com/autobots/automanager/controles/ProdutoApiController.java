package com.autobots.automanager.controles;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.hateoas.HateoasAdicionador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoApiController {

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private HateoasAdicionador hateoasAdicionador;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Map<String, Object>> obterProdutosPorEmpresa(@PathVariable Long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        
        
        hateoasAdicionador.adicionarLink(empresa.getServicos(), ControleServico.class, "self");
        hateoasAdicionador.adicionarLink(empresa.getMercadorias(), ControleMercadoria.class, "self");
        
        Map<String, Object> response = new HashMap<>();
        response.put("servicos", empresa.getServicos());
        response.put("mercadorias", empresa.getMercadorias());
        
        
        hateoasAdicionador.adicionarLink(response, ProdutoApiController.class, "self");
        
        return ResponseEntity.ok(response);
    }
}