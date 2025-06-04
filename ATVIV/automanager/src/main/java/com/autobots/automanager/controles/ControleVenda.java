package com.autobots.automanager.controles;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/vendas")
public class ControleVenda {

	@Autowired
	private RepositorioVenda repositorio;

	@Autowired
	private RepositorioUsuario usuarioRepositorio;

	@PostMapping("/cadastrar")
	@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE')")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda, Principal principal) {
		Usuario vendedor = usuarioRepositorio.findByCredencialNomeUsuario(principal.getName()).orElse(null);
		if (vendedor == null) {
			return new ResponseEntity<>("Vendedor não encontrado", HttpStatus.NOT_FOUND);
		}
		venda.setFuncionario(vendedor);
		venda.setCadastro(new Date());
		repositorio.save(venda);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/minhas-vendas")
	@PreAuthorize("hasAnyRole('VENDEDOR', 'GERENTE')")
	public ResponseEntity<List<Venda>> obterMinhasVendas(Principal principal) {
		Usuario vendedor = usuarioRepositorio.findByCredencialNomeUsuario(principal.getName()).orElse(null);
		if (vendedor == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Venda> vendas = repositorio.findByFuncionario(vendedor);
		return new ResponseEntity<>(vendas, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	public ResponseEntity<?> atualizarVenda(@PathVariable Long id, @RequestBody Venda vendaAtualizada) {
		Optional<Venda> venda = repositorio.findById(id);
		if (venda.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Venda v = venda.get();
		v.setCliente(vendaAtualizada.getCliente());
		v.setMercadorias(vendaAtualizada.getMercadorias());
		v.setServicos(vendaAtualizada.getServicos());

		repositorio.save(v);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	public ResponseEntity<?> deletarVenda(@PathVariable Long id) {
		Optional<Venda> venda = repositorio.findById(id);
		if (venda.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		repositorio.delete(venda.get());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}