package com.autobots.automanager.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCredencialNomeUsuario(String nomeUsuario);
    
    @Query("SELECT u FROM Usuario u WHERE :perfil MEMBER OF u.perfis")
    List<Usuario> findByPerfil(@Param("perfil") Perfil perfil);
    
    List<Usuario> findByPerfisContaining(Perfil perfil);
}
