package com.autobots.automanager.controles;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioCliente;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/clientes")
public class ControleCliente {

	@Autowired
	private RepositorioCliente repositorio;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
		Cliente cliente = repositorio.findById(id).orElse(null);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/meu-cadastro")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<Cliente> obterMeuCadastro(Principal principal) {
		Optional<Usuario> usuario = repositorioUsuario.findByCredencialNomeUsuario(principal.getName());

		if (usuario.isEmpty() || !(usuario.get() instanceof Cliente)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Cliente cliente = (Cliente) usuario.get();
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
		Cliente cliente = repositorio.findById(id).orElse(null);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		cliente.setNome(clienteAtualizado.getNome());
		cliente.setTelefones(clienteAtualizado.getTelefones());
		cliente.setEndereco(clienteAtualizado.getEndereco());

		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
		Cliente cliente = repositorio.findById(id).orElse(null);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		repositorio.delete(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}