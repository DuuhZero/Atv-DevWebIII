package com.autobots.automanager.controladores;

import com.autobots.automanager.entitades.*;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaControlador {

    @Autowired
    private RepositorioEmpresa repositorio;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Empresa>>> listarEmpresas() {
        List<EntityModel<Empresa>> empresas = repositorio.findAll().stream()
            .map(empresa -> {
                empresa.getTelefones().size();
                empresa.getUsuarios().size();
                empresa.getMercadorias().size();
                empresa.getServicos().size();
                empresa.getVendas().size();
                
                return EntityModel.of(empresa,
                    linkTo(methodOn(EmpresaControlador.class).obterEmpresa(empresa.getId())).withSelfRel(),
                    linkTo(methodOn(EmpresaControlador.class).listarEmpresas()).withRel("lista-completa-empresas"),
                    linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withRel("usuarios-da-empresa"),
                    linkTo(methodOn(MercadoriaControlador.class).listarMercadorias()).withRel("mercadorias-da-empresa"),
                    linkTo(methodOn(ServicoControlador.class).listarServicos()).withRel("servicos-da-empresa"),
                    linkTo(methodOn(VendaControlador.class).listarVendas()).withRel("vendas-da-empresa")
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(empresas,
            linkTo(methodOn(EmpresaControlador.class).listarEmpresas()).withSelfRel()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Empresa>> obterEmpresa(@PathVariable Long id) {
        Empresa empresa = repositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        empresa.getTelefones().size();
        empresa.getUsuarios().size();
        empresa.getMercadorias().size();
        empresa.getServicos().size();
        empresa.getVendas().size();

        return ResponseEntity.ok(EntityModel.of(empresa,
            linkTo(methodOn(EmpresaControlador.class).obterEmpresa(id)).withSelfRel(),
            linkTo(methodOn(EmpresaControlador.class).listarEmpresas()).withRel("lista-completa-empresas"),
            linkTo(methodOn(UsuarioControlador.class).listarUsuarios()).withRel("usuarios-da-empresa"),
            linkTo(methodOn(MercadoriaControlador.class).listarMercadorias()).withRel("mercadorias-da-empresa"),
            linkTo(methodOn(ServicoControlador.class).listarServicos()).withRel("servicos-da-empresa"),
            linkTo(methodOn(VendaControlador.class).listarVendas()).withRel("vendas-da-empresa")
        ));
    }


    @PostMapping
    public ResponseEntity<EntityModel<Empresa>> cadastrarEmpresa(@RequestBody Empresa empresa) {
        Date dataAtual = new Date();
        empresa.setCadastro(dataAtual);
        
        if (empresa.getMercadorias() != null) {
            empresa.getMercadorias().forEach(mercadoria -> {
                mercadoria.setCadastro(dataAtual);
            });
        }
        
        Empresa salva = repositorio.save(empresa);
        return ResponseEntity.created(URI.create("/empresas/" + salva.getId()))
            .body(EntityModel.of(salva,
                linkTo(methodOn(EmpresaControlador.class).obterEmpresa(salva.getId())).withSelfRel()
            ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa atualizacao) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        atualizacao.setId(id);
        repositorio.save(atualizacao);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirEmpresa(@PathVariable Long id) {
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}