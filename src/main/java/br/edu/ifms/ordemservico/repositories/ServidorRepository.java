package br.edu.ifms.ordemservico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifms.ordemservico.entities.Servidor;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long>{

}
