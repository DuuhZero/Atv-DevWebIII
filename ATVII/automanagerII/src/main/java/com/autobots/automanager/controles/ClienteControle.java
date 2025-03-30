package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.*;
import com.autobots.automanager.modelos.*;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {
    
    @Autowired
    private ClienteRepositorio repositorio;
    
    @Autowired
    private ClienteSelecionador selecionador;
    
    @Autowired
    private AdicionadorLinkCliente adicionadorLink;
    
    @Autowired
    private AdicionadorLinkDocumento adicionadorLinkDocumento;
    
    @Autowired
    private AdicionadorLinkEndereco adicionadorLinkEndereco;
    
    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obterCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        adicionadorLink.adicionarLink(cliente);
        EntityModel<Cliente> recurso = EntityModel.of(cliente);
        return new ResponseEntity<>(recurso, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<EntityModel<Cliente>> clientesModel = clientes.stream()
            .map(cliente -> {
                adicionadorLink.adicionarLink(cliente);
                return EntityModel.of(cliente);
            })
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Cliente>> recursos = CollectionModel.of(clientesModel);
        recursos.add(Link.of("/clientes").withSelfRel());
        
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        if (cliente.getId() == null) {
            repositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<?> atualizarCliente(@RequestBody Cliente atualizacao) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Cliente cliente = repositorio.findById(atualizacao.getId()).orElse(null);
        
        if (cliente != null) {
            ClienteAtualizador atualizador = new ClienteAtualizador();
            atualizador.atualizar(cliente, atualizacao);
            repositorio.save(cliente);
            status = HttpStatus.OK;
        }
        
        return new ResponseEntity<>(status);
    }

    @DeleteMapping
    public ResponseEntity<?> excluirCliente(@RequestBody Cliente exclusao) {
        Cliente cliente = repositorio.findById(exclusao.getId()).orElse(null);
        
        if (cliente != null) {
            repositorio.delete(cliente);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<CollectionModel<EntityModel<Documento>>> obterDocumentosCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Documento> documentos = cliente.getDocumentos();
        adicionadorLinkDocumento.adicionarLink(documentos);
        
        List<EntityModel<Documento>> documentosModel = documentos.stream()
            .map(documento -> {
                adicionadorLinkDocumento.adicionarLink(documento);
                return EntityModel.of(documento);
            })
            .collect(Collectors.toList());
            
        CollectionModel<EntityModel<Documento>> recursos = CollectionModel.of(documentosModel);
        recursos.add(Link.of("/clientes/" + id + "/documentos").withSelfRel());
        
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    @GetMapping("/{id}/endereco")
    public ResponseEntity<EntityModel<Endereco>> obterEnderecoCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente == null || cliente.getEndereco() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Endereco endereco = cliente.getEndereco();
        adicionadorLinkEndereco.adicionarLink(endereco);
        
        EntityModel<Endereco> recurso = EntityModel.of(endereco);
        recurso.add(Link.of("/clientes/" + id + "/endereco").withSelfRel());
        
        return new ResponseEntity<>(recurso, HttpStatus.OK);
    }

    @GetMapping("/{id}/telefones")
    public ResponseEntity<CollectionModel<EntityModel<Telefone>>> obterTelefonesCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Telefone> telefones = cliente.getTelefones();
        adicionadorLinkTelefone.adicionarLink(telefones);
        
        List<EntityModel<Telefone>> telefonesModel = telefones.stream()
            .map(telefone -> {
                adicionadorLinkTelefone.adicionarLink(telefone);
                return EntityModel.of(telefone);
            })
            .collect(Collectors.toList());
            
        CollectionModel<EntityModel<Telefone>> recursos = CollectionModel.of(telefonesModel);
        recursos.add(Link.of("/clientes/" + id + "/telefones").withSelfRel());
        
        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }
}