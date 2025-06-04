package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuarios")
public class ControleUsuario {

	@Autowired
	private RepositorioUsuario repositorio;

	@GetMapping("/listar")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/buscar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isPresent()) {
			return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/criar")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		usuario.getCredencial().setSenha(codificador.encode(usuario.getCredencial().getSenha()));
		repositorio.save(usuario);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Usuario u = usuario.get();
		u.setNome(usuarioAtualizado.getNome());
		u.getCredencial().setNomeUsuario(usuarioAtualizado.getCredencial().getNomeUsuario());

		if (usuarioAtualizado.getCredencial().getSenha() != null
				&& !usuarioAtualizado.getCredencial().getSenha().isEmpty()) {
			BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
			u.getCredencial().setSenha(codificador.encode(usuarioAtualizado.getCredencial().getSenha()));
		}

		repositorio.save(u);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
		Optional<Usuario> usuario = repositorio.findById(id);
		if (usuario.isPresent()) {
			repositorio.delete(usuario.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
