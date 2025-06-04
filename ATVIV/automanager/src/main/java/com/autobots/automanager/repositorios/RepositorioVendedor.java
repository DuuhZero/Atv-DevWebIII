package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Vendedor;

@Repository
public interface RepositorioVendedor extends JpaRepository<Vendedor, Long> {
}
